package com.mobiquity.parser;

import com.mobiquity.entities.Data;
import com.mobiquity.entities.Item;
import com.mobiquity.exception.APIException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    private static final  String ITEMREGEX = "(?<id>\\d+),(?<weight>\\d+\\.\\d+),\u20AC(?<cost>\\d+)";
    private static final  String ITEMREGEX2 = "(?<id>\\d+),(?<weight>\\d+),\u20AC(?<cost>\\d+)";
    private final String lineRegex = String.format("(\\d+) : ((\\(%s)\\s*\\)+)", ITEMREGEX);
    private final Pattern linePattern = Pattern.compile(lineRegex);
    private final Pattern itemPattern = Pattern.compile(ITEMREGEX);
    private final Pattern itemPattern2 = Pattern.compile(ITEMREGEX2);

    private static Parser parser;

    private Parser() {
    }

    public static synchronized Parser getInstance() {
        if (parser == null)
            parser = new Parser();
        return parser;
    }

    public List<Data> parse(String filePath) throws APIException {
        this.validateFilePath(filePath);

        List<Data> data;
        try {
            data = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)
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

        int capacity = getCapacityFromStringData(line);
        List<Item> items = getItemsFromStringData(line);

        return new Data(capacity, items);
    }

    private boolean validateProblemInString(String line) {
        var linetMatcher = linePattern.matcher(line);

        return linetMatcher.find();
    }

    private int getCapacityFromStringData(String line) {
        String[] capacityFromItems = line.split(" : ");
        return Integer.parseInt(capacityFromItems[0]);
    }

    private List<Item> getItemsFromStringData(String line) {
        var stringItems = line.split(":")[1];
        var itemMatcher = itemPattern.matcher(stringItems);
        if (!itemMatcher.find())
            itemMatcher = itemPattern2.matcher(stringItems);

        List<Item> items = new ArrayList<>();
        while (itemMatcher.find()) {
            var triplet = new Item(
                    Integer.parseInt(itemMatcher.group("id")),
                    Float.parseFloat(itemMatcher.group("weight")),
                    Integer.parseInt(itemMatcher.group("cost")));
            items.add(triplet);
        }
        return items;
    }
}