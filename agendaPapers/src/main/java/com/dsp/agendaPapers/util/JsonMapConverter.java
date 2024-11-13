package com.dsp.agendaPapers.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonMapConverter implements AttributeConverter<List<Map<String, Object>>, String> {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<Map<String, Object>> attribute) {
		try {
			return mapper.writeValueAsString(attribute);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error converting List<Map<String, Object>> to JSON string", e);
		}
	}

	@Override
	public List<Map<String, Object>> convertToEntityAttribute(String dbData) {
		try {
			return mapper.readValue(dbData, new TypeReference<>() {
			});
		} catch (Exception e) {
			throw new IllegalArgumentException("Error converting JSON string to List<Map<String, Object>>", e);
		}
	}
}
