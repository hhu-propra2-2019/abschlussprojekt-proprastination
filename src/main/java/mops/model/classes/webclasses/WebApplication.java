package mops.model.classes.webclasses;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebApplication {
    private String module;
    private int workload;
    private int priority;
    private double grade;
    private String lecturer;
    private String semester;
    private String role;
    private String comment;

}
