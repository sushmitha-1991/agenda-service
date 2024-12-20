package com.dsp.agendaPapers.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Map;

@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

//    @Override
//    public String convertToDatabaseColumn(Map<String, Object> attribute) {
//        try {
//            return objectMapper.writeValueAsString(attribute);
//        } catch (JsonProcessingException e) {
//            throw new IllegalArgumentException("Error converting map to JSON string", e);
//        }
//    }
//
//    @Override
//    public Map<String, Object> convertToEntityAttribute(String dbData) {
//        try {
//            return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
//        } catch (IOException e) {
//            throw new IllegalArgumentException("Error converting JSON string to map", e);
//        }
//    }}
    //above code is working but not maintained the null

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        // Check if the map is null and return null instead of throwing an exception
        if (attribute == null) {
            return null;  // Return null if the attribute is null
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Log the error and throw a more informative exception
            throw new IllegalArgumentException("Error converting map to JSON string", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        // Check if the database column data is null and return null instead of throwing an exception
        if (dbData == null) {
            return null;  // Return null if the database data is null
        }

        try {
            // Using a TypeReference to deserialize into a Map
            return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            // Log the error and throw a more informative exception
            throw new IllegalArgumentException("Error converting JSON string to map", e);
        }
    }

}

