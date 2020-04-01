package mops.model.classes.webclasses;

import lombok.Builder;
import lombok.Data;
import mops.model.classes.Applicant;
import mops.model.classes.Module;
import mops.model.classes.Priority;
import mops.model.classes.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder(toBuilder = true)
@Data
public class OrgaApplication {
    private long id;
    private int minHours;
    private int finalHours;
    private int maxHours;
    private Module module;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private double grade;
    private String lecturer;
    private String semester;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String comment;
    private Applicant applicant;
}
