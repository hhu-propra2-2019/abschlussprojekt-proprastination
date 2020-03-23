package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Application;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class WebEvaluation {
    private Application application;
    private int hours;
    private String comment;
    private int priority;
}
