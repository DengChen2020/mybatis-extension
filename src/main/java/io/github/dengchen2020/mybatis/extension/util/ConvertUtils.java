package io.github.dengchen2020.mybatis.extension.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * @author dengchen
 */
public class ConvertUtils {

    static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)
            .setTimeZone(TimeZone.getDefault());

    public static List<?> convertList(List<Map<String, Object>> inputList, Class<?> targetType) {
        return inputList.stream()
                .map(stringObjectMap -> convertObject(stringObjectMap, targetType))
                .collect(Collectors.toList());
    }

    public static Object convertObject(Map<String, Object> inputMap, Class<?> targetType) {
        if (targetType.isInstance(inputMap)) {
            return inputMap;
        }
        if (inputMap.size() == 1) {
            for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
                Object value = entry.getValue();
                if (value.getClass() == targetType) {
                    return value;
                }
                return objectMapper.convertValue(value, targetType);
            }
        }
        Map<String, Object> map = new HashMap<>((int) Math.ceil(inputMap.size() * 2 / (double) 0.75f));
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String convertedKey = convertToCamelCase(key);
            map.put(convertedKey, value);
        }
        return objectMapper.convertValue(map, TypeFactory.defaultInstance().constructType(targetType));
    }

    /**
     * 下划线转驼峰
     *
     * @param name 字段名
     * @return 驼峰命名
     */
    private static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        boolean toUpperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char currentChar = name.charAt(i);
            if (currentChar == '_') {
                toUpperCase = true;
            } else {
                if (toUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    toUpperCase = false;
                } else {
                    result.append(currentChar);
                }
            }
        }
        return result.toString();
    }

}
