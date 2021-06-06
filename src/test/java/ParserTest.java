import com.mobiquity.entities.Data;
import com.mobiquity.entities.Item;
import com.mobiquity.exception.APIException;
import com.mobiquity.util.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

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
        int expectedTripletInEachProblem =1;
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
        assertEquals(parsedTripletsInFirstProblem.size(),expectedTripletInEachProblem);
        assertEquals(parsedTripletsInSecondProblem.size(),expectedTripletInEachProblem);
        assertEquals(parsedTripletsInFirstProblem,firstProblem.getItems());
        assertEquals(parsedTripletsInSecondProblem,secondProblem.getItems());
    }

    @Test
    public void throwApiExceptionWhenNotExistingFileInputPassed(){
        String expectedExceptionMessage = "Invalid parameter: file not exists";


        APIException exception = assertThrows(APIException.class,
                () -> Parser.getInstance().parse("/not/existing/input.file"));

        assertEquals(expectedExceptionMessage, expectedExceptionMessage);
    }

    @Test
    public void throwExceptionWhenNullInputFilePassed() {
        String expectedExceptionMessage = "Invalid parameter: File path required";

        APIException exception = assertThrows(APIException.class,
                () ->Parser.getInstance().parse(null));

        assertEquals(expectedExceptionMessage,exception.getMessage());
    }

    @Test
    public void throwExceptionWhenParsingInvalidLine(){
        String expectedExceptionMessage ="(9,89.95,\u20AC78) : 75";

        APIException exception = assertThrows(APIException.class,
                () ->Parser.getInstance().parse(invalidContentInputFile.getAbsolutePath()));

        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void throwExceptionWhenParsingCostWithNoEuroSign(){
        String expectedExceptionMessage = "81 : (1,53.38,45)";

        APIException exception = assertThrows(APIException.class,
                () ->Parser.getInstance().parse(noEuroSignInputFile.getAbsolutePath()));

        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }
}
