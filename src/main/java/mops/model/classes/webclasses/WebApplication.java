package mops.model.classes.webclasses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Priority;
import mops.model.classes.Role;


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
    public static final int MAX_COMMENT_LENGTH = 1000;
    private long id;
    private String module;
    private int minHours;
    private int maxHours;
    private Priority priority;
    private String module;
    @NotNull
    @Positive
    private int workload;
    @NotNull
    @Positive
    private double grade;
    @NotBlank
    private String lecturer;
    @NotBlank
    private String semester;
    private Role role;
    @Size(max = MAX_COMMENT_LENGTH)
    private String comment;

}
