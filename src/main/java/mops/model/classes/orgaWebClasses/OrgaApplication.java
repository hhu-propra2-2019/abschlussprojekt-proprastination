package mops.model.classes.orgaWebClasses;

import lombok.Builder;
import lombok.Data;
import mops.model.classes.Applicant;

@Builder(toBuilder = true)
@Data
public class OrgaApplication {
    private long applicationId;
    private int minHours;
    private int finalHours;
    private int maxHours;
    private int priority;
    private double grade;
    private String module;
    private String lecturer;
    private String semester;
    private String role;
    private String comment;
    private Applicant applicant;
}
