package com.dteliukov.profitsoftlab2.parsers.impl;

import com.dteliukov.profitsoftlab2.dtos.DishJsonObjDto;
import com.dteliukov.profitsoftlab2.parsers.ReaderParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Component for reading DishJsonObjDto data from a JSON stream using Jackson Streaming API.
 */
@Component
@RequiredArgsConstructor
public class DishJacksonStreamingReaderParser implements ReaderParser<DishJsonObjDto> {

    private final ObjectMapper objectMapper;

    @Override
    public List<DishJsonObjDto> read(InputStream inputStream) {
        JsonFactory jsonFactory = objectMapper.getFactory();
        List<DishJsonObjDto> dishes = new LinkedList<>();

        try (JsonParser jsonParser = jsonFactory.createParser(inputStream)) {
            while (!jsonParser.isClosed()) {
                JsonToken jsonToken = jsonParser.nextToken();
                if (JsonToken.START_ARRAY.equals(jsonToken)) {
                    dishes = new ArrayList<>();
                } else if (JsonToken.END_ARRAY.equals(jsonToken)) {
                    break;
                } else if (JsonToken.START_OBJECT.equals(jsonToken)) {
                    dishes.add(readDish(jsonParser));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return dishes;
    }

    private DishJsonObjDto readDish(JsonParser jsonParser) throws IOException {
        String name = null;
        String description = null;
        int price = 0;
        int weight = 0;
        float calories = 0;
        long categoryId = 0;
        List<String> ingredients = null;
        List<String> cuisines = null;
        List<String> dietarySpecifics = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();

            if (fieldName != null) {

                switch (fieldName) {
                    case "name":
                        name = jsonParser.getValueAsString();
                        break;
                    case "description":
                        description = jsonParser.getValueAsString();
                        break;
                    case "price":
                        price = jsonParser.getValueAsInt();
                        break;
                    case "weight":
                        weight = jsonParser.getValueAsInt();
                        break;
                    case "calories":
                        calories = jsonParser.getFloatValue();
                        break;
                    case "categoryId":
                        categoryId = jsonParser.getValueAsLong();
                        break;
                    case "ingredients":
                        ingredients = readStringList(jsonParser);
                        break;
                    case "cuisines":
                        cuisines = readStringList(jsonParser);
                        break;
                    case "dietarySpecifics":
                        dietarySpecifics = readStringList(jsonParser);
                        break;
                }
            }
        }

        return new DishJsonObjDto(name, description, price, weight, calories, categoryId, ingredients, cuisines, dietarySpecifics);
    }

    private static List<String> readStringList(JsonParser jsonParser) throws IOException {
        List<String> attributeValues = new LinkedList<>();

        jsonParser.nextToken(); // move to START_ARRAY
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            jsonParser.nextToken();
        }
        while (jsonParser.getCurrentToken() != JsonToken.END_ARRAY) {
            attributeValues.add(jsonParser.getText());
            jsonParser.nextToken();
        }

        return attributeValues;
    }
}
