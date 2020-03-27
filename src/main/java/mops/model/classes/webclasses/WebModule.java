package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Module;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WebModule {
    private String name;
    private String shortName;
    private String profSerial;
    private String deadlineDate;
    private String deadlineTime;
    private LocalDateTime deadline;
    private String sevenHourLimit;
    private String nineHourLimit;
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
                .deadlineDate(this.deadlineDate)
                .deadlineTime(this.deadlineTime)
                .deadline(this.deadline)
                .sevenHourLimit(this.sevenHourLimit)
                .nineHourLimit(this.nineHourLimit)
                .seventeenHourLimit(this.seventeenHourLimit)
                .build();
    }
}
