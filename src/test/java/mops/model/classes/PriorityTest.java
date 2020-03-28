package mops.model.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityTest {

    @Test
    void getLabelVeryHigh() {
        String label = Priority.VERYHIGH.getLabel();

        assertEquals("++", label);
    }

    @Test
    void getLabelHigh() {
        String label = Priority.HIGH.getLabel();

        assertEquals("+", label);
    }

    @Test
    void getLabelNeutral() {
        String label = Priority.NEUTRAL.getLabel();

        assertEquals("O", label);
    }

    @Test
    void getLabelNegative() {
        String label = Priority.NEGATIVE.getLabel();

        assertEquals("-", label);
    }

    @Test
    void getValueVeryHigh() {
        int value = Priority.VERYHIGH.getValue();

        assertEquals(1, value);
    }

    @Test
    void getValueHigh() {
        int value = Priority.HIGH.getValue();

        assertEquals(2, value);
    }

    @Test
    void getValueNeutral() {
        int value = Priority.NEUTRAL.getValue();

        assertEquals(3, value);
    }

    @Test
    void getValueNegative() {
        int value = Priority.NEGATIVE.getValue();

        assertEquals(4, value);
    }
}