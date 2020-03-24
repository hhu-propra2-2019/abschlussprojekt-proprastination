package mops.model.classes.webclasses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Module;
import mops.model.classes.Priority;
import mops.model.classes.Role;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebApplication {
    private long id;
    private Module module;
    private int minHours;
    private int maxHours;
    private Priority priority;
    private double grade;
    private String lecturer;
    private String semester;
    private Role role;
    private String comment;

}
