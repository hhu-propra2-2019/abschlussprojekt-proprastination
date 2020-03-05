package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;


@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true)
public class Application {

    private final int hours;
    @NonNull
    private final String module;
    private final double grade;
    private final String lecturer;
    private final String semester;
    private final String comment;
    private final Role role;

}
