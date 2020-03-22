package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebApplicant {
    public static final int MAX_COMMENT_LENGTH = 1000;
    @NotBlank
    private String birthplace;
    @NotBlank
    private String gender;
    @NotBlank
    @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")
    private String birthday;
    @NotBlank
    private String nationality;
    @NotBlank
    private String course;
    @NotBlank
    @Pattern(regexp = "neueinstellung|weiterbeschaeftigung|wiedereinstellung")
    private String status;
    @Size(max = MAX_COMMENT_LENGTH)
    private String comment;
}
