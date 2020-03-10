package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
@ToString
public class Applicant {
    private final String name;
    private final String birthplace;
    private final Address address;
    private final String birthday;
    private final String nationality;
    private final String course;
    private final Status status;
    private final Certificate cert;
    @Singular
    private final List<Application> applications;

}
