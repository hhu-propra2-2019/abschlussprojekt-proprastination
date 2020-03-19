package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebApplicant {
    @NotNull
    private String birthplace;
    @NotNull
    private String gender;
    @NotNull
    private String birthday;
    @NotNull
    private String nationality;
    @NotNull
    private String course;
    @NotNull
    private String status;
    private String comment;
}
