package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
public class Evaluation {
    private final Application application;
    private final Applicant applicant;
    private final String comment;
    private final int priority;
}
