package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationTest {
    Application application;

    @BeforeEach
    void setup() {
        //We do not Test the Applicant and Application here.
        application = Application.builder()
                .module("ProPra2")
                .build();
    }

    @Test
    void builder() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is awesome!")
                .priority(1)
                .build();

        assertThat(evaluation)
                .hasFieldOrPropertyWithValue("application", application)
                .hasFieldOrPropertyWithValue("comment", "He is awesome!")
                .hasFieldOrPropertyWithValue("priority", 1);

    }

    @Test
    void testToString() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is not awesome!")
                .priority(3)
                .build();

        String evalString = evaluation.toString();
        String expected = "Evaluation(application=Application(hours=0, module=ProPra2, priority=0, grade=0.0,"
                + " lecturer=null, semester=null, role=null, comment=null, applicant=null), comment=He is not awesome!, priority=3)";
        assertThat(evalString).isEqualTo(expected);
    }

    @Test
    void testEquals() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is the best")
                .priority(2)
                .build();

        Evaluation evaluation2 = Evaluation.builder()
                .application(application)
                .comment("He is the best")
                .priority(2)
                .build();

        Evaluation evaluation3 = Evaluation.builder()
                .application(application)
                .comment("He is awesome!")
                .priority(1)
                .build();

        assertThat(evaluation).isEqualTo(evaluation2);
        assertThat(evaluation).isNotEqualTo(evaluation3);


    }
}