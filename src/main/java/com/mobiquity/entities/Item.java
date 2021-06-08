package com.mobiquity.entities;

import java.util.Objects;

public class Item {
    private final int id;
    private final float weight;
    private final int cost;

    public Item(float weight, int cost) {
        this(0, weight, cost);
    }

    public Item(int id, float weight, int cost) {
        this.id = id;
        this.weight = weight;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public float getWeight() {
        return weight;
    }

    public int getCost() {
        return cost;
    }
    public float getRatio() {
        return this.cost / this.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var item = (Item) o;
        return  Float.compare(item.weight, weight) == 0 &&
                cost == item.cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, cost);
    }

    @Override
    public String toString() {
        return "Item[" +
                "id=" + id +
                ", weight=" + weight +
                ", cost=" + cost +
                ']';
    }
}
