package com.mobiquity;

import com.mobiquity.entities.Data;
import com.mobiquity.entities.Item;
import com.mobiquity.parser.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    static File twoProblemSingleTripletFile;
    static File invalidContentInputFile;
    static File noEuroSignInputFile;

    @BeforeAll
    static void initialize() {
        twoProblemSingleTripletFile = new File(
                PackerTest.class
                        .getClassLoader()
                        .getResource("two_problems_single_triplet.txt")
                        .getFile());
        invalidContentInputFile = new File(
                PackerTest.class
                        .getClassLoader()
                        .getResource("invalid_content.txt")
                        .getFile());
        noEuroSignInputFile = new File(
                PackerTest.class
                        .getClassLoader()
                        .getResource("cost_value_no_euro_sign.txt")
                        .getFile());
    }

    @Test
    void onlyOnceInstanceCreatedWhenGetInstance(){

        Parser parserFirstInstance =Parser.getInstance();
        Parser parserSecondInstance =Parser.getInstance();

        assertEquals(parserFirstInstance,parserSecondInstance);
    }

    @Test
    void returnListOfProblemWhenMultilineInputFileParsed(){

        int expectedListSize = 2;
        int expectedTripletInEachProblem =0;
        int firstProblemMaxCapacity=81;

        Data firstProblem= new Data(firstProblemMaxCapacity);
        firstProblem.getItems().add(new Item(1,53.38f,45));

        int secondProblemMaxCapacity=8;
        Data secondProblem= new Data(secondProblemMaxCapacity);
        secondProblem.getItems().add(new Item(1,15.3f,34));


        List<Data> systemUnderTest = Parser.getInstance().parse(twoProblemSingleTripletFile.getAbsolutePath());
        int parsedMaxCapacityInFirstProblem = systemUnderTest.get(0).getMaxCapacity();
        List<Item> parsedTripletsInFirstProblem = systemUnderTest.get(0).getItems();
        int parsedMaxCapacityInSecondProblem = systemUnderTest.get(1).getMaxCapacity();
        List<Item> parsedTripletsInSecondProblem = systemUnderTest.get(1).getItems();



        assertEquals(expectedListSize,systemUnderTest.size());
        assertEquals(parsedMaxCapacityInFirstProblem,firstProblemMaxCapacity);
        assertEquals(parsedMaxCapacityInSecondProblem,secondProblemMaxCapacity);
    }
}
