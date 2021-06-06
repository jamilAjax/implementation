package com.mobiquity.util;

import com.mobiquity.entities.Data;
import com.mobiquity.entities.Item;
import com.mobiquity.exception.APIException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    private final String itemRegex = "(?<id>\\d+),(?<weight>\\d+\\.\\d+),\u20AC(?<cost>\\d+)";
    private final String lineRegex = String.format("(\\d+) : ((\\(%s)\\s*\\)+)", itemRegex);
    private final Pattern linePattern = Pattern.compile(lineRegex);
    private final Pattern itemPattern = Pattern.compile(itemRegex);

    private static Parser parser;

    private Parser() {
    }

    public static Parser getInstance() {
        if (parser == null) {
            synchronized (Parser.class) {
                if (parser == null)
                    parser = new Parser();
            }
        }
        return parser;
    }

    public List<Data> parse(String filePath) throws APIException {
        this.validateFilePath(filePath);

        List<Data> data;
        try {
            data = Files.lines(Paths.get(filePath))
                    .map(this::lineToData)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new APIException(e.toString());
        }

        return data;
    }

    private void validateFilePath(String filePath) throws APIException {

        Optional
                .ofNullable(filePath)
                .orElseThrow(() -> new APIException("Invalid parameter: File path required"));

        boolean fileExists = Paths.get(filePath).toFile().exists();

        if (!fileExists)
            throw new APIException("Invalid parameter: file not exists");
    }

    private Data lineToData(String line) throws APIException {

        if (!this.validateProblemInString(line))
            throw new APIException(String.format("Can not parse line: %s ", line));

        int capacity = getCapacityFromStringData(line);
        List<Item> items = getItemsFromStringData(line);

        return new Data(capacity, items);
    }

    private boolean validateProblemInString(String line) {
        Matcher linetMatcher = linePattern.matcher(line);

        return linetMatcher.find();
    }

    private int getCapacityFromStringData(String line) {
        String[] capacityFromItems = line.split(" : ");
        return Integer.parseInt(capacityFromItems[0]);
    }

    private List<Item> getItemsFromStringData(String line) {
        String stringItems = line.split(":")[1];
        Matcher itemMatcher = itemPattern.matcher(stringItems);
        List<Item> items = new ArrayList<>();
        while (itemMatcher.find()) {
            Item triplet = new Item(
                    Integer.parseInt(itemMatcher.group("id")),
                    Float.parseFloat(itemMatcher.group("weight")),
                    Integer.parseInt(itemMatcher.group("cost")));
            items.add(triplet);
        }
        return items;
    }
}