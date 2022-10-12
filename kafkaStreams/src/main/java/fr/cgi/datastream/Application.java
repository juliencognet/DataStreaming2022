package fr.cgi.datastream;

import fr.cgi.datastream.data.BuildingAgregateDTO;
import fr.cgi.datastream.data.BuildingDTO;
import fr.cgi.datastream.data.serdes.BuildingAgregateSerdes;
import fr.cgi.datastream.data.serdes.BuildingSerdes;
import fr.cgi.datastream.tools.CdcToPOJO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.Properties;

@Slf4j
public class Application {


    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "data-streaming-ks");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        BuildingSerdes buildingSerdes = new BuildingSerdes();
        BuildingAgregateSerdes buildingAgregateSerdes = new BuildingAgregateSerdes();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> energy = builder.stream("T2-enriched-data");
        energy
                .mapValues(e -> CdcToPOJO.toPojo(e, BuildingDTO.class))
                .filter((k,v) -> k != null && v != null)

                .groupByKey(Grouped.with(Serdes.String(), buildingSerdes))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)))
                .aggregate(BuildingAgregateDTO::new, Application::compute, Materialized.with(Serdes.String(), buildingAgregateSerdes))
                .toStream()
                .map((Windowed<String> key, BuildingAgregateDTO buildingDTO) -> new KeyValue<>(key.key(),buildingDTO))
                .to("T3-computed-data", Produced.with(Serdes.String(), buildingAgregateSerdes));

        Topology build = builder.build();
        log.info(build.describe().toString());
        KafkaStreams streams = new KafkaStreams(build, props);
        streams.start();


    }

    private static BuildingAgregateDTO compute(String key, BuildingDTO value, BuildingAgregateDTO agg){
        agg.setBuilding(key);
        Float safeArea = value.getArea() == null || value.getArea() == 0 ? 1f : value.getArea();
        Float safeValue = value.getCorrMeterValue()== null  ? 0f : value.getArea();
        Float newValue = (safeValue/ safeArea);
        agg.setResult(agg.getResult() + newValue);
        return agg;
    }

}