package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Address {
    private final String street;
    private final String city;
    private final String country;
    private final int zipcode;
}
