package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Priority;
import mops.model.classes.Role;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebList {
    private long id;
    private int priority;
    private Priority studentPriority;
    private Role role;
    private int finalHours;
    private double grade;
    private int minHours;
    private int maxHours;
    private String uniserial;
    private String firstName;
    private String surname;

}
