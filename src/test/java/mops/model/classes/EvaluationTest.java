package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationTest {
    Application application;

    @BeforeEach
    void setup() {
        Module module = Module.builder()
                .deadline(Instant.ofEpochSecond(100l))
                .name("Info4")
                .build();
        //We do not Test the Applicant and Application here.
        application = Application.builder()
                .module(module)
                .build();
    }

    @Test
    void builder() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is awesome!")
                .priority(Priority.SehrHoch)
                .build();

        assertThat(evaluation)
                .hasFieldOrPropertyWithValue("application", application)
                .hasFieldOrPropertyWithValue("comment", "He is awesome!")
                .hasFieldOrPropertyWithValue("priority", Priority.SehrHoch);

    }

    @Test
    void testToString() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is not awesome!")
                .priority(Priority.Neutral)
                .build();

        String evalString = evaluation.toString();
        String expected = "Evaluation(application=Application(minHours=0, finalHours=0, maxHours=0, " +
                "module=Module(name=Info4, deadline=1970-01-01T00:01:40Z, shortName=null, profName=null, " +
                "sevenHourLimit=null, nineHourLimit=null, seventeenHourLimit=null, hourLimit=null), priority=null," +
                " grade=0.0, lecturer=null, semester=null, role=null, comment=null, applicant=null), hours=0," +
                " comment=He is not awesome!, priority=Neutral)";
        assertThat(evalString).isEqualTo(expected);
    }

    @Test
    void testEquals() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is the best")
                .priority(Priority.SehrHoch)
                .build();

        Evaluation evaluation2 = Evaluation.builder()
                .application(application)
                .comment("He is the best")
                .priority(Priority.SehrHoch)
                .build();

        Evaluation evaluation3 = Evaluation.builder()
                .application(application)
                .comment("He is awesome!")
                .priority(Priority.Negative)
                .build();

        assertThat(evaluation).isEqualTo(evaluation2);
        assertThat(evaluation).isNotEqualTo(evaluation3);


    }
}