package fr.cgi.datastream.data.serdes;

import fr.cgi.datastream.data.BuildingDTO;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class BuildingSerdes implements Serde<BuildingDTO> {
    private BuildingSerializer serializer = new BuildingSerializer();
    private BuildingDeserializer deserializer = new BuildingDeserializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer<BuildingDTO> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<BuildingDTO> deserializer() {
        return deserializer;
    }
}
