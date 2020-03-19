package mops.model.classes.webclasses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class WebApplicant {
    private String firstName;
    private String surname;
    private String birthplace;
    private String gender;
    private String birthday;
    private String nationality;
    private String course;
    private String status;
    private String comment;
}
