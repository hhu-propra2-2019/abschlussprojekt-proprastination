package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder(builderClassName = "ModuleBuilder", toBuilder = true)
public class Module {
    private final String name;
    private final String shortName;
    private final String profName;
    private final String sevenHourLimit;
    private final String nineHourLimit;
    private final String seventeenHourLimit;
    private final String hourLimit;
}
