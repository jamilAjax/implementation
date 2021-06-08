package com.mobiquity;

import com.mobiquity.entities.Data;
import com.mobiquity.entities.Item;
import com.mobiquity.exception.APIException;
import com.mobiquity.service.SolutionConcrete;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CumulativePackingTest {

    @Test
    void returnItemWithLessWeightWhenTwoItemOfSameCost(){
        Data data = new Data(10);
        Item optimalItem = new Item(2, 9.99f, 100);
        data.getItems().add(new Item(1,10.0f,100));
        data.getItems().add(optimalItem);

        SolutionConcrete serviceUnderTest = new SolutionConcrete();
        List<Item> triplets = serviceUnderTest.getMaximumItems(data);

        assertEquals(optimalItem,triplets.get(0));
    }

    @Test
    void returnItemWithHighCostWhenTwoItemOfSameWeight(){
        Data data = new Data(10);
        Item optimalItem = new Item(2, 10.0f, 100);
        data.getItems().add(new Item(1,10.0f,99));
        data.getItems().add(optimalItem);

        SolutionConcrete serviceUnderTest = new SolutionConcrete();
        List<Item> triplets = serviceUnderTest.getMaximumItems(data);

        assertEquals(optimalItem,triplets.get(0));
    }

    @Test
    void returnEmptyListWhenItemsNotFittingPackageCapacity(){
        Data data = new Data(10);
        data.getItems().add(new Item(1,88.0f,99));
        data.getItems().add(new Item(2, 99.0f, 100));

        SolutionConcrete serviceUnderTest = new SolutionConcrete();
        List<Item> triplets = serviceUnderTest.getMaximumItems(data);

        assertEquals(0,triplets.size());
    }

    @Test
    void ThrowExceptionWhenInvalidWeightValue(){
        String expectedExceptionMessage =String.format("Invalid problem : Max item weight possible is %d",
                SolutionConcrete.MAX_TRIPLET_WEIGHT);
        Data data = new Data(10);
        data.getItems().add(new Item(1,110.0f,99));

        SolutionConcrete serviceUnderTest = new SolutionConcrete();
        APIException exception = assertThrows(APIException.class, () -> serviceUnderTest.getMaximumItems(data));

        assertEquals(expectedExceptionMessage,exception.getMessage());

    }

    @Test
    void ThrowExceptionWhenInvalidCostValue(){
        String expectedExceptionMessage =String.format("Invalid problem : Max item cost possible is %d",
                SolutionConcrete.MAX_TRIPLET_COST);
        Data data = new Data(10);
        data.getItems().add(new Item(1,80.0f,110));

        SolutionConcrete serviceUnderTest = new SolutionConcrete();
        APIException exception = assertThrows(APIException.class, () -> serviceUnderTest.getMaximumItems(data));

        assertEquals(expectedExceptionMessage,exception.getMessage());

    }

    @Test
    void ThrowExceptionWhenInvalidNumberOfItemInProblem(){
        String expectedExceptionMessage =String.format("Invalid problem : Max items possible is %d",
                SolutionConcrete.MAX_TRIPLETS_SIZE_IN_PROBLEM);
        List<Item> triplets = IntStream.range(0,SolutionConcrete.MAX_TRIPLETS_SIZE_IN_PROBLEM + 1)
                .mapToObj(i-> new Item(i,80.0f,90))
                .collect(Collectors.toList());
        Data data = new Data(10,triplets);

        SolutionConcrete serviceUnderTest = new SolutionConcrete();
        APIException exception = assertThrows(APIException.class, () -> serviceUnderTest.getMaximumItems(data));

        assertEquals(expectedExceptionMessage,exception.getMessage());

    }


}