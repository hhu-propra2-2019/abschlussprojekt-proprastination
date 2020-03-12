package mops.model.classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder(builderClassName = "DistributionBuilder", toBuilder = true)
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = Distribution.DistributionBuilder.class)
public class Distribution {
    private final String module;
    private final List<Applicant> employees;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DistributionBuilder {

    }
}
