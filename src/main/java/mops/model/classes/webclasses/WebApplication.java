package mops.model.classes.webclasses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebApplication {
    public static final int MAX_PRIORITY_VALUE = 4;
    public static final int MAX_COMMENT_LENGTH = 1000;
    private String module;
    @NotNull
    @Positive
    private int workload;
    @NotNull
    @Positive
    @Max(MAX_PRIORITY_VALUE)
    private int priority;
    @NotNull
    @Positive
    private double grade;
    @NotBlank
    private String lecturer;
    @NotBlank
    private String semester;
    @NotBlank
    @Pattern(regexp = "corrector|correctorandtutor|nomatter")
    private String role;
    @Size(max = MAX_COMMENT_LENGTH)
    private String comment;

}
