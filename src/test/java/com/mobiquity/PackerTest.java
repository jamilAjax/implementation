package com.mobiquity;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.Packer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackerTest {

    static File multiLineInputFile;
    static File singleLineInputFile;

    @BeforeAll
    static void initialize() {
        multiLineInputFile = new File(
                Objects.requireNonNull(PackerTest.class
                        .getClassLoader()
                        .getResource("example_input.txt"))
                        .getFile());
        singleLineInputFile = new File(
                Objects.requireNonNull(PackerTest.class
                        .getClassLoader()
                        .getResource("single_line_problem.txt"))
                        .getFile());
    }

    @Test
    void throwExceptionWhenNullInputFilePassed() {
        String expectedExceptionMessage = "Invalid parameter: File path required";

        APIException exception = assertThrows(APIException.class, () -> Packer.pack(null));

        assertEquals(expectedExceptionMessage,exception.getMessage());
    }

    @Test
    void returnOptimalStringResultsWhenValidSinglelineInputFilePassed() {
        String expectedResult = "2,3";


        String result = Packer.pack(singleLineInputFile.getAbsolutePath());

        assertEquals(expectedResult, result);
    }

    @Test
    void returnOptimalStringResultsWhenValidMultilineInputFilePassed() {
        String expectedResult = (new StringBuilder())
                .append("4")
                .append(System.lineSeparator())
                .append("-")
                .append(System.lineSeparator())
                .append("2,7")
                .append(System.lineSeparator())
                .append("8,9")
                .toString();


        String result = Packer.pack(multiLineInputFile.getAbsolutePath());

        assertEquals(expectedResult, result);
    }
}