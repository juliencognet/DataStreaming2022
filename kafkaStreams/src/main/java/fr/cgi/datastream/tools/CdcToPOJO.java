package fr.cgi.datastream.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CdcToPOJO {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CdcToPOJO() {
    }

    /**
     * This method returns
     *      + either an empty list in case of any error
     *      + a list of a singleton element if the deserialization works.
     */
    public static <T> List<T> toPojoList(String value, Class<T> tClass) {

        List<T> pojos = new ArrayList<>();
        // ObjectMapper instantiation

        //TODO : return null as null

        // If the value is null or empty send back an empty list
        if (StringUtils.isEmpty(value)) {
            log.info("La valeur est soit vide ou nulle");
            return pojos;
        }

        // Don't block the stream in case of any deserialization error
        try {
            pojos.add(objectMapper.readValue(value, tClass));
        } catch (IOException e) {
            log.info("Erreur de deserialisation de la valeur : {} en {}", value, tClass.getName(), e);
        }
        return pojos;
    }

    public static <T> T toPojo(String value, Class<T> tClass) {

        if (value == null) {
            return null;
        }
        T t = null;
        try {
            t = objectMapper.readValue(value, tClass);
        } catch (IOException e) {
            log.info("Erreur de deserialisation de la valeur : {} en {}", value, tClass.getName(), e);
        }
        return t;
    }

}
