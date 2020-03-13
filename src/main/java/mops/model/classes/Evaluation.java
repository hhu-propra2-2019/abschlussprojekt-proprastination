package mops.model.classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder(builderClassName = "EvaluationBuilder", toBuilder = true)
@JsonDeserialize(builder = Evaluation.EvaluationBuilder.class)
public class Evaluation {
    private final Application application;
    private final Applicant applicant;
    private final String comment;
    private final int priority;

    @JsonPOJOBuilder(withPrefix = "")
    public static class EvaluationBuilder {

    }
}
