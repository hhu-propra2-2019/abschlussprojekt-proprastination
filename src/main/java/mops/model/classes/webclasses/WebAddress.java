package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class WebAddress {
    @NotBlank
    private String street;
    private String number;
    @NotBlank
    private String city;
    @NotBlank
    private String zipcode;
}
