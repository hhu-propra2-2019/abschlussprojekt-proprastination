package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Module;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WebModule {
    @NotBlank
    private String name;
    @NotBlank
    private String shortName;
    @NotBlank
    private String profSerial;
    @NotBlank
    @Pattern(regexp = "0|([1-9][0-9]*)")
    private String sevenHourLimit;
    @NotBlank
    @Pattern(regexp = "0|([1-9][0-9]*)")
    private String nineHourLimit;
    @NotBlank
    @Pattern(regexp = "0|([1-9][0-9]*)")
    private String seventeenHourLimit;
}
