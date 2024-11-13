package com.dsp.agendaPapers.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MapJsonConverter implements AttributeConverter<Map<String, Object>, String> {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		// Register the JavaTimeModule to handle Java 8 time types
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Override
	public String convertToDatabaseColumn(Map<String, Object> attribute) {
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting map to JSON", e);
		}
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, Map.class);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error converting JSON to map", e);
		}
	}
}
