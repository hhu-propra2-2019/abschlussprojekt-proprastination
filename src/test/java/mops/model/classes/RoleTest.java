package mops.model.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {

    @Test
    void getLabelProofreader() {
        String label = Role.PROOFREADER.getLabel();

        assertEquals("Korrektor", label);
    }

    @Test
    void getLabelTutor() {
        String label = Role.TUTOR.getLabel();

        assertEquals("Tutor", label);
    }

    @Test
    void getLabelBoth() {
        String label = Role.BOTH.getLabel();

        assertEquals("Korrektor und Tutor", label);
    }
}