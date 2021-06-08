package com.mobiquity.service;

import com.mobiquity.entities.Data;
import com.mobiquity.entities.Item;
import com.mobiquity.entities.CumulativeSet;
import com.mobiquity.exception.APIException;
import com.mobiquity.commons.Commons;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionConcrete implements SolutionInterface {
    Merger merger;

    public static final int MAX_TRIPLET_WEIGHT = 100;
    public static final int MAX_TRIPLET_COST = 100;
    public static final int MAX_TRIPLETS_SIZE_IN_PROBLEM = 15;

    public SolutionConcrete() {
        this.merger = new Merger();
    }

    @Override
    public List<Item> getMaximumItems(Data data) {

        this.validateData(data);
        List<CumulativeSet> sets = this.buildCumulativeSets(data);

        return this.findOptimalTripletsInCumulativeSets(data, sets);
    }
    @Override
    public String  getOptimalItemIdsInString(Data data) {
        List<Item> optimalTriplets = this.getMaximumItems(data);
        String output = optimalTriplets.stream()
                .map(Item::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        return Commons.defaultIfEmpty(output, "-");
    }

    private void validateData(Data data) {
        this.validateTripletCosts(data);
        this.validateTripletWeights(data);
        this.validateTripletMaxSize(data);
    }

    private void validateTripletMaxSize(Data data) {
        if (data.getItems().size() > MAX_TRIPLETS_SIZE_IN_PROBLEM)
            throw new APIException(
                    String.format("Invalid problem : Max items possible is %d", MAX_TRIPLETS_SIZE_IN_PROBLEM));

    }

    private void validateTripletWeights(Data data) {
        boolean anyInvalidWeight =  data.getItems()
                .stream()
                .map(Item::getWeight)
                .anyMatch(weight -> weight > MAX_TRIPLET_WEIGHT || weight <= 0.0f);

        if (anyInvalidWeight)
            throw new APIException(
                    String.format("Invalid problem : Max item weight possible is %d", MAX_TRIPLET_WEIGHT));

    }

    private void validateTripletCosts(Data data) {
        boolean anyInvalidCost = data.getItems()
                .stream()
                .map(Item::getCost)
                .anyMatch(cost -> cost > MAX_TRIPLET_COST || cost <= 0);

        if (anyInvalidCost)
            throw new APIException(
                    String.format("Invalid problem : Max item cost possible is %d", MAX_TRIPLET_COST));
    }

    private List<CumulativeSet> buildCumulativeSets(Data data) {

        data.getItems().add(new Item(0, 0, 0));
        this.sortProblemTripletsWithRatio(data);
        List<CumulativeSet> sets = this.getInitializedCumulativeSets(data);
        this.removeOverCapacityTriplets(data);


        for (var i = 1; i < data.getItems().size(); i++) {
            CumulativeSet currentSet = sets.get(i - 1);
            List<Item> extendedSet = this.extend(currentSet, data.getItems().get(i));
            List<Item> mergedTriplets = merger.merge(currentSet.getItems(), extendedSet);
            sets.add(new CumulativeSet(mergedTriplets, currentSet.getMaximumCapacity()));
        }

        return sets;
    }

    private List<Item> findOptimalTripletsInCumulativeSets(Data data, List<CumulativeSet> sets) {
        int lastSetIndex = sets.size() - 1;    
        int lastSetItem = sets.get(lastSetIndex).getItems().size() - 1;    
        var lastItem = sets.get(lastSetIndex).getItems().get(lastSetItem);
        List<Item> solution = new ArrayList<>();

        int cumulativeCost = lastItem.getCost();
        float cumulativeWeight = lastItem.getWeight();
        var prevItem = lastItem;

        for (var i = lastSetIndex - 1; i >= 0; i--) {
            int prevSetIndex = i + 1;
            CumulativeSet currSet = sets.get(i);
            boolean found = currSet.exists(prevItem);
            if (!found) {
                solution.add(data.getItems().get(prevSetIndex));
                cumulativeCost -= data.getItems().get(prevSetIndex).getCost();
                cumulativeWeight = Commons.round(cumulativeWeight - data.getItems().get(prevSetIndex).getWeight());
                prevItem = new Item(cumulativeWeight, cumulativeCost);
            }    
        }
        return solution;
    }

    private void sortProblemTripletsWithRatio(Data data) {
        data.getItems().sort(Comparator.comparing(Item::getRatio).reversed());
    }

    private List<CumulativeSet> getInitializedCumulativeSets(Data data) {
        List<CumulativeSet> sets = new ArrayList<>();

        var cumulativeSet = new CumulativeSet(data.getMaxCapacity());
        cumulativeSet.getItems().add(data.getItems().get(0));
        sets.add(cumulativeSet);
        return sets;
    }
    
    private void removeOverCapacityTriplets(Data data) {
        data.getItems().removeIf(t -> t.getWeight() > data.getMaxCapacity());
    }
    
    private List<Item> extend(CumulativeSet set, Item item) {
        return set.getItems().stream()
                .map(t -> this.add(t, item))
                .filter(t -> this.validCapacity(t, set.getMaximumCapacity()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean validCapacity(Item item, int maximumCapacity) {
        return item.getWeight() <= maximumCapacity;
    }

    private Item add(Item item1, Item item2) {
        int cumulativeCost = item1.getCost() + item2.getCost();
        float cumulativeWeight = item1.getWeight() + item2.getWeight();

        return new Item(item2.getId(), cumulativeWeight, cumulativeCost);
    }

    private static class Merger {

        private int firstPointer;
        private int firstMaxIndex;
        private int firstLastItemCost;

        private int secondPointer;
        private int secondMaxIndex;
        private int secondLastItemCost;
        List<Item> firstItems;
        List<Item> secondItems;
        
        public void initialize(List<Item> firstItems, List<Item> secondItems) {

            firstPointer = 0;
            firstMaxIndex = firstItems.size() - 1;
            firstLastItemCost = firstItems.get(firstMaxIndex).getCost();

            secondPointer = 0;
            secondMaxIndex = secondItems.size() - 1;
            secondLastItemCost = secondItems.get(secondMaxIndex).getCost();

            this.firstItems = firstItems;
            this.secondItems = secondItems;
        }
        
        public List<Item> merge(List<Item> firstItems, List<Item> secondItems) {

            this.initialize(firstItems, secondItems);
            List<Item> result = new ArrayList<>();

            while (arePointersNotTraversedCompletely()) {
                if (areBothPointersInRange()) {
                    var firstItem = this.firstItems.get(firstPointer);
                    Item secondTriplet = this.secondItems.get(secondPointer);

                    if (firstItem.getWeight() < secondTriplet.getWeight()) {
                        result.add(firstItem);    // Add item; can't be dominated by other item
                        firstPointer++;
                        moveSecondPointerUntilNotDominated(firstItem, secondTriplet);

                    } else if (firstItem.getWeight() == secondTriplet.getWeight()) {
                        moveDominatedPointerInCaseEqualWeights(firstItem, secondTriplet);

                    } else {
                        result.add(secondTriplet);    //  Add other item, can't be dominated by item
                        secondPointer++;
                        moveFirstPointerInCaseEqualWeights(firstItem, secondTriplet);
                    }
                } else if (firstPointer > firstMaxIndex) {    // Only other items left to consider
                    addSecondTripletToResultIfNotDominated(result);
                } else {    // indexOther > maxIndexOther. Only items left to consider
                    addFirstTripletToResultIfNotDominated(result);
                }

            }
            return result;
        }

        private void addFirstTripletToResultIfNotDominated(List<Item> result) {
            while (firstPointer <= firstMaxIndex) {
                var firstItem = firstItems.get(firstPointer);
                if (firstItem.getCost() > secondLastItemCost)
                    result.add(firstItem);
                firstPointer++;
            }
        }

        private void addSecondTripletToResultIfNotDominated(List<Item> result) {
            while (secondPointer <= secondMaxIndex) {
                var secondItem = secondItems.get(secondPointer);
                if (secondItem.getCost() > firstLastItemCost)
                    result.add(secondItem);
                secondPointer++;
            }
        }

        private void moveFirstPointerInCaseEqualWeights(Item firstItem, Item secondItem) {
            while (firstItem.getCost() < secondItem.getCost() && firstPointer <= firstMaxIndex) {    // item dominated; skip it
                if (firstPointer == firstMaxIndex) {
                    ++firstPointer;
                    break;
                }
                firstItem = firstItems.get(++firstPointer);
            }
        }
        
        private void moveDominatedPointerInCaseEqualWeights(Item thisItem, Item secondItem) {
            if (thisItem.getCost() >= secondItem.getCost())    
                secondPointer++;
            else
                firstPointer++;                            
        }

        private void moveSecondPointerUntilNotDominated(Item firstItem, Item secondItem) {
            while (secondItem.getCost() < firstItem.getCost() && secondPointer <= secondMaxIndex) {    
                if (secondPointer == secondMaxIndex) {
                    ++secondPointer;
                    break;
                }
                secondItem = secondItems.get(++secondPointer);
            }
        }
        
        private boolean arePointersNotTraversedCompletely() {
            return firstPointer <= firstMaxIndex || secondPointer <= secondMaxIndex;
        }

        private boolean areBothPointersInRange() {
            return firstPointer <= firstMaxIndex && secondPointer <= secondMaxIndex;
        }
    }
}
