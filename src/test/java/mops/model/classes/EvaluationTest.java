package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationTest {
    Application application;

    @BeforeEach
    void setup() {
        Module module = Module.builder()
                .deadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
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
                .priority(Priority.VERYHIGH)
                .build();

        assertThat(evaluation)
                .hasFieldOrPropertyWithValue("application", application)
                .hasFieldOrPropertyWithValue("comment", "He is awesome!")
                .hasFieldOrPropertyWithValue("priority", Priority.VERYHIGH);

    }

    @Test
    void testToString() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is not awesome!")
                .priority(Priority.NEUTRAL)
                .build();

        String evalString = evaluation.toString();
        String expected = "Evaluation(application=Application(minHours=0, finalHours=0, maxHours=0, " +
                "module=Module(name=Info4, deadlineDate=null, deadlineTime=null, deadline=1970-01-01T00:01:40," +
                " shortName=null, profSerial=null, sevenHourLimit=null, nineHourLimit=null, seventeenHourLimit=null)," +
                " priority=null, grade=0.0, lecturer=null, semester=null, role=null, comment=null), hours=0," +
                " comment=He is not awesome!, priority=NEUTRAL)";
        assertThat(evalString).isEqualTo(expected);
    }

    @Test
    void testEquals() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .comment("He is the best")
                .priority(Priority.VERYHIGH)
                .build();

        Evaluation evaluation2 = Evaluation.builder()
                .application(application)
                .comment("He is the best")
                .priority(Priority.VERYHIGH)
                .build();

        Evaluation evaluation3 = Evaluation.builder()
                .application(application)
                .comment("He is awesome!")
                .priority(Priority.NEGATIVE)
                .build();

        assertThat(evaluation).isEqualTo(evaluation2);
        assertThat(evaluation).isNotEqualTo(evaluation3);


    }
}
