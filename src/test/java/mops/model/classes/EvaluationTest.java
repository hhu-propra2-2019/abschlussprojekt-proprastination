package mops.model.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationTest {
    Applicant applicant;
    Application application;

    @BeforeEach
    void setup() {
        //We do not Test the Applicant and Application here.
        applicant = Applicant.builder()
                .name("Hans")
                .build();

        application = Application.builder()
                .module("ProPra2")
                .applicantusername("hans111")
                .build();
    }

    @Test
    void builder() {
        Evaluation evaluation = Evaluation.builder()
                .applicant(applicant)
                .application(application)
                .comment("He is awesome!")
                .priority(1)
                .build();

        assertThat(evaluation)
                .hasFieldOrPropertyWithValue("applicant", applicant)
                .hasFieldOrPropertyWithValue("application", application)
                .hasFieldOrPropertyWithValue("comment", "He is awesome!")
                .hasFieldOrPropertyWithValue("priority", 1);

    }

    @Test
    void toBuilder() {
        Evaluation evaluation = Evaluation.builder()
                .applicant(applicant)
                .application(application)
                .comment("He is awesome!")
                .priority(1)
                .build();

        Evaluation.EvaluationBuilder evaluationBuilder = evaluation.toBuilder();
        Evaluation evaluation2 = evaluationBuilder.build();

        assertThat(evaluation2).isEqualTo(evaluation);

    }

    @Test
    void testToString() {
        Evaluation evaluation = Evaluation.builder()
                .applicant(applicant)
                .application(application)
                .comment("He is not awesome!")
                .priority(3)
                .build();

        String evalString = evaluation.toString();
        String expected = "Evaluation(application=Application(applicantusername=hans111, hours=0, module=ProPra2, priority=0, grade=0.0, "
                + "lecturer=null, semester=null, comment=null, role=null), applicant=Applicant(name=Hans, birthplace=null, address=null, birthday=null, nationality=null, course=null, status=null, certs=null, applications=[]), comment=He is not awesome!, priority=3)";
        assertThat(evalString).isEqualTo(expected);
    }

    @Test
    void testEquals() {
        Evaluation evaluation = Evaluation.builder()
                .applicant(applicant)
                .application(application)
                .comment("He is the best")
                .priority(2)
                .build();

        Evaluation evaluation2 = Evaluation.builder()
                .applicant(applicant)
                .application(application)
                .comment("He is the best")
                .priority(2)
                .build();

        Evaluation evaluation3 = Evaluation.builder()
                .applicant(applicant)
                .application(application)
                .comment("He is awesome!")
                .priority(1)
                .build();

        assertThat(evaluation).isEqualTo(evaluation2);
        assertThat(evaluation).isNotEqualTo(evaluation3);


    }
}