package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EvaluationTest {
    Application application;

    @BeforeEach
    void setup() {
        Module module = Module.builder()
                .applicantDeadline(LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC))
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
                .priority(Priority.VERYHIGH)
                .build();

        assertThat(evaluation)
                .hasFieldOrPropertyWithValue("application", application)
                .hasFieldOrPropertyWithValue("priority", Priority.VERYHIGH);

    }

    @Test
    void testToString() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .priority(Priority.NEUTRAL)
                .build();

        String evalString = evaluation.toString();
        String expected = "Evaluation(application=Application(minHours=0, finalHours=0, maxHours=0, " +
                "module=Module(name=Info4, applicantDeadlineDate=null, applicantDeadlineTime=null, applicantDeadline=1970-01-01T00:01:40," +
                " orgaDeadlineDate=null, orgaDeadlineTime=null, orgaDeadline=null," +
                " shortName=null, profSerial=null, sevenHourLimit=null, nineHourLimit=null, seventeenHourLimit=null)," +
                " priority=null, grade=0.0, lecturer=null, semester=null, role=null, comment=null), hours=0," +
                " priority=NEUTRAL)";
        assertThat(evalString).isEqualTo(expected);
    }

    @Test
    void testEquals() {
        Evaluation evaluation = Evaluation.builder()
                .application(application)
                .priority(Priority.VERYHIGH)
                .build();

        Evaluation evaluation2 = Evaluation.builder()
                .application(application)
                .priority(Priority.VERYHIGH)
                .build();

        Evaluation evaluation3 = Evaluation.builder()
                .application(application)
                .priority(Priority.NEGATIVE)
                .build();

        assertThat(evaluation).isEqualTo(evaluation2);
        assertThat(evaluation).isNotEqualTo(evaluation3);
    }

    @Test
    void testNoArgsConstructor() {
        Evaluation emptyEvaluation = new Evaluation();

        assertNull(emptyEvaluation.getApplication());
        assertEquals(0, emptyEvaluation.getHours());
        assertNull(emptyEvaluation.getPriority());
    }
}
