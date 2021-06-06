package com.mobiquity.util;

import java.util.List;
import java.util.stream.Collectors;

public class NewLineFormatter implements ListFormatter{
    @Override
    public String format(List<String> list) {
        return list.stream().collect(Collectors.joining(System.lineSeparator()));
    }
}
