package com.mobiquity.entities;

import java.util.ArrayList;
import java.util.List;

public class CumulativeSet {
    private final List<Item> items;
    private final int maximumCapacity;

    public CumulativeSet(List<Item> triplets, int maximumCapacity) {
        this.items = triplets;
        this.maximumCapacity = maximumCapacity;
    }

    public CumulativeSet(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }
    public boolean exists(Item item){

        boolean found = false;
        int cursor= this.getItems().size()-1;
        for (int j= cursor; j >= 0; j--) {
            Item currItem= this.getItems().get(j);
            if (currItem.equals(item)) {
                found= true;
                break;
            }
            if (currItem.getWeight() < item.getWeight())
                break;
        }
        return found;
    }

    @Override
    public String toString() {
        return "CumulativeSet{" +
                "maximumCapacity=" + maximumCapacity +
                ", triplets=" + items +
                '}';
    }
}