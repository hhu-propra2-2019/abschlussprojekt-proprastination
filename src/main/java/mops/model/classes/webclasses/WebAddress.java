package mops.model.classes.webclasses;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class WebAddress {
    private String street;
    private String houseNumber;
    private String city;
    private int zipcode;
}
