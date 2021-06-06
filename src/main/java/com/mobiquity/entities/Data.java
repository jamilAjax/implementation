package com.mobiquity.entities;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private final List<Item> items;
    private final int maxCapacity;

    public Data(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        items = new ArrayList<>();
    }

    public Data(int maxCapacity, List<Item> items) {
        this.maxCapacity = maxCapacity;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "maxCapacity=" + maxCapacity +
                ", items=" + items +
                '}';
    }
}
