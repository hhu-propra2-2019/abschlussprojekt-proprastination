package mops.model.classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;
import org.junit.experimental.categories.Category;


@EqualsAndHashCode
@ToString
@Getter
@Builder(builderClassName = "ApplicationBuilder", toBuilder = true)
@JsonDeserialize(builder = Application.ApplicationBuilder.class)
public class Application {
    @NonNull
    private final String applicantusername;
    private final int hours;
    @NonNull
    private final String module;
    private final int priority;
    private final double grade;
    private final String lecturer;
    private final String semester;
    private final String comment;
    private final Role role;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ApplicationBuilder {

    }

}
