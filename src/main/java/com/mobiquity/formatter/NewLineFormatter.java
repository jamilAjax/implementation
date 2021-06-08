package com.mobiquity.formatter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewLineFormatter implements ListFormatter{
    @Override
    public String format(List<String> list) {
        return list.stream()
                .map(this::sortString)
                .collect(Collectors.joining(System.lineSeparator())).replace(" ","");
    }

    private String sortString(String items){
        String replaceItems = items.replace(" ","");
        String[] sortItems = replaceItems.split(",");
        return Arrays.stream(sortItems).sorted().collect(Collectors.joining(","));
    }
}
