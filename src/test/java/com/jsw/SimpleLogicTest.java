package com.jsw;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleLogicTest {

    @Test
    void shouldAddTwoNumbersCorrectly(){

        int numberOne=2;
        int numberTwo=3;

        int result = numberOne+numberTwo;

        assertEquals(5,result);
    }

}
