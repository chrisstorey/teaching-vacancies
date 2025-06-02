package com.example.twelvefactorapp.model.converter;

import com.example.twelvefactorapp.model.enums.WorkingPattern;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class WorkingPatternsConverter implements AttributeConverter<List<WorkingPattern>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<WorkingPattern> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream()
                .map(WorkingPattern::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<WorkingPattern> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(String::trim)
                .map(WorkingPattern::valueOf)
                .collect(Collectors.toList());
    }
}
