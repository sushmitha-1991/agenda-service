package com.dsp.agendaPapers.pcquestionbranchinput.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JsonMapsConverters implements AttributeConverter<List<Map<String, Object>>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = (Logger) LoggerFactory.getLogger(JsonMapsConverters.class);

    static {
        // Register the JavaTimeModule to handle Java 8 time types
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String convertToDatabaseColumn(List<Map<String, Object>> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.info("Error converting map to JSON: {}");
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public List<Map<String, Object>> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            // Try to parse as a list of maps
            return objectMapper.readValue(dbData, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            logger.info("Failed to parse JSON as List<Map<String, Object>>. Trying Map<String, Object> format.");

            try {
                // If parsing as a list fails, attempt to parse as a map of maps and convert to a list
                Map<String, Object> mapData = objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
                return List.of(mapData); // Wrap the map in a list
            } catch (IOException innerException) {
                logger.info("Error converting JSON to List<Map<String, Object>>: {}");
                throw new IllegalArgumentException("Error converting JSON to List<Map<String, Object>>", innerException);
            }
        }
    }

}
