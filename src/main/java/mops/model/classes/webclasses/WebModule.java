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
    @Pattern(regexp = "[0-9]+")
    private String sevenHourLimit;
    @NotBlank
    @Pattern(regexp = "[0-9]+")
    private String nineHourLimit;
    @NotBlank
    @Pattern(regexp = "[0-9]+")
    private String seventeenHourLimit;

    /**
     * @return returns module as String Array
     */
    public String[] toStringArray() {
        return new String[]{name, shortName, profSerial, sevenHourLimit,
                nineHourLimit, seventeenHourLimit};
    }

    /**
     * Return Module
     * @return Module
     */
    public Module toModule() {
        return Module.builder()
                .name(this.name)
                .shortName(this.shortName)
                .profSerial(this.profSerial)
                .sevenHourLimit(this.sevenHourLimit)
                .nineHourLimit(this.nineHourLimit)
                .seventeenHourLimit(this.seventeenHourLimit)
                .build();
    }
}
