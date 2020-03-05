package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true)
public class Application {

    @NotNull
    private final int hours;
    @NotNull
    private final String module;
    @NotNull
    private final double grade;
    private final String lecturer;
    private final String semester;
    private final String comment;
    private final Role role;

}
