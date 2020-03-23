package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebApplicant {
    private String birthplace;
    private String gender;
    private String birthday;
    private String nationality;
    private String course;
    private String status;
    private String comment;
}
