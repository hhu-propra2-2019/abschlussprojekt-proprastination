package mops.model.classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(builderClassName = "CertificateBuilder", toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(builder = Certificate.CertificateBuilder.class)
public class Certificate {
    private final String name;
    private final String university;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateBuilder {

    }
}
