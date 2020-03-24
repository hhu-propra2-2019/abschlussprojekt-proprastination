package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Module;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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

    /**
     * Return Module
     * @return Module
     */
    public Module toModule() {
        Module m = Module.builder()
                .name(this.name)
                .shortName(this.shortName)
                .profName(this.profName)
                .sevenHourLimit(this.sevenHourLimit)
                .nineHourLimit(this.nineHourLimit)
                .seventeenHourLimit(this.seventeenHourLimit)
                .hourLimit(this.hourLimit)
                .build();
        return m;
    }
}
