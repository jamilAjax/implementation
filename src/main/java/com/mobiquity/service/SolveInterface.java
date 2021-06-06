package com.mobiquity.service;

import com.mobiquity.entities.Data;
import com.mobiquity.entities.Item;

import java.util.List;

public interface SolveInterface {
    List<Item> getMaximumItems(Data data);

    String getOptimalItemIdsInString(Data data);
}
