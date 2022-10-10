package fr.cgi.datastream.data.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cgi.datastream.data.BuildingDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class BuildingDeserializer implements Closeable, AutoCloseable, Deserializer<BuildingDTO> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public BuildingDTO deserialize(String topic, byte[] bytes) {
        try {
            return mapper.readValue(bytes, BuildingDTO.class);
        } catch (Exception exception) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter( writer );
            exception.printStackTrace( printWriter );
            printWriter.flush();

            String stackTrace = writer.toString();
            log.info(stackTrace);
            throw new IllegalArgumentException("Error reading bytes", exception);
        }

    }

    @Override
    public void close() {

    }
}