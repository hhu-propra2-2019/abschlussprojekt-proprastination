package mops.model.classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(builderClassName = "AddressBuilder", toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(builder = Address.AddressBuilder.class)
public class Address {
    private final String street;
    private final String houseNumber;
    private final String city;
    private final String country;
    private final int zipcode;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AddressBuilder {
    }
}
