package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "ModuleBuilder", toBuilder = true)
public class WebModule {
    private String name;
    private String shortName;
    private String profName;
    private String sevenHourLimit;
    private String nineHourLimit;
    private String seventeenHourLimit;
    private String hourLimit;

    /**
     * @return returns module as String Array
     */
    public String[] toStringArray() {
        return new String[]{name, shortName, profName, sevenHourLimit,
                nineHourLimit, seventeenHourLimit, hourLimit};
    }
}
