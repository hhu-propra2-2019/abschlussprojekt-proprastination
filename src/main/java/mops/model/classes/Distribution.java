package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
@ToString
public class Distribution {
    private final String module;
    private final Applicant[] employees;
}
