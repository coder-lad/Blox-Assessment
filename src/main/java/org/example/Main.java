package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;

public class Main {

    public static Object parseJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true); // To allow NaN and Infinity values
        JsonNode jsonNode = objectMapper.readTree(json);
        return parseJsonNode(jsonNode);
    }

    private static Object parseJsonNode(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            return parseObjectNode(jsonNode);
        } else if (jsonNode.isArray()) {
            return parseArrayNode(jsonNode);
        } else if (jsonNode.isBigInteger()) {
            return jsonNode.bigIntegerValue();
        } else if (jsonNode.isBigDecimal()) {
            return jsonNode.decimalValue();
        } else if (jsonNode.isInt()) {
            return jsonNode.intValue();
        } else if (jsonNode.isLong()) {
            return jsonNode.longValue();
        } else if (jsonNode.isDouble()) {
            return jsonNode.doubleValue();
        } else if (jsonNode.isBoolean()) {
            return jsonNode.booleanValue();
        } else if (jsonNode.isTextual()) {
            return jsonNode.textValue();
        } else if (jsonNode.isNull()) {
            return null;
        } else {
            throw new IllegalArgumentException("Unsupported JSON node type: " + jsonNode);
        }
    }

    private static Map<String, Object> parseObjectNode(JsonNode jsonNode) {
        Map<String, Object> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            result.put(field.getKey(), parseJsonNode(field.getValue()));
        }
        return result;
    }

    private static List<Object> parseArrayNode(JsonNode jsonNode) {
        List<Object> result = new ArrayList<>();
        for (JsonNode node : jsonNode) {
            result.add(parseJsonNode(node));
        }
        return result;
    }

    public static void main(String[] args) {
        String jsonString = "{\"name\":\"Milind\", \"age\":25, \"balance\":12345.67, \"isMember\":true, \"details\":{\"height\":175, \"weight\":62}, \"scores\":[95, 85, 77]}";
        try {
            Object result = parseJson(jsonString);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}