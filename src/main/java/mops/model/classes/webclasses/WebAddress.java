package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Max;


@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class WebAddress {
    public static final int MAX_ZIPCODE = 99999;
    @NotBlank
    private String street;
    private String number;
    @NotBlank
    private String city;
    @NotNull
    @PositiveOrZero
    @Max(MAX_ZIPCODE)
    private int zipcode;
}
