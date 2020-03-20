package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class WebAddress {
    public static final int MAX_ZIPCODE = 99999;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotNull
    @PositiveOrZero
    @Max(MAX_ZIPCODE)
    private int zipcode;
}
