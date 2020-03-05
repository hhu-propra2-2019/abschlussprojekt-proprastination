package mops.model.classes;

import lombok.*;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true)
public class Application {

    @NonNull
    private final int hours;
    @NonNull
    private final String module;
    @NonNull
    private final double grade;
    private final String lecturer;
    private final String semester;
    private final String comment;
    private final Role role;

}
