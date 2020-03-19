package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebApplicant {
    @NotBlank
    private String birthplace;
    @NotBlank
    private String gender;
    @NotBlank
    private String birthday;
    @NotBlank
    private String nationality;
    @NotBlank
    private String course;
    @NotBlank
    private String status;
    private String comment;
}
