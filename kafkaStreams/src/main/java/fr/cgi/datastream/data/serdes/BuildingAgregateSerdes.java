package fr.cgi.datastream.data.serdes;

import fr.cgi.datastream.data.BuildingAgregateDTO;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class BuildingAgregateSerdes implements Serde<BuildingAgregateDTO> {
    private final BuildingAgregateSerializer serializer = new BuildingAgregateSerializer();
    private final BuildingAgregateDeserializer deserializer = new BuildingAgregateDeserializer();

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
    public Serializer<BuildingAgregateDTO> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<BuildingAgregateDTO> deserializer() {
        return deserializer;
    }
}
