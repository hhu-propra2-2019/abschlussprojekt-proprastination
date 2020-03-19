package mops.model.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder(builderClassName = "EvaluationBuilder", toBuilder = true)
public class Module {
    private final String name;
    private final String shortName;
    private final String profName;
    private final String hourLimit;
    private final String personLimit;

    /**
     * Constructor for Module.
     * @param pName
     * @param pShortName
     * @param pProfName
     * @param phourLimit
     * @param pPersonLimits
     */

    public Module(final String pName, final String pShortName, final String pProfName,
                  final String phourLimit, final String pPersonLimits) {
        this.name = pName;
        this.shortName = pShortName;
        this.profName = pProfName;
        this.hourLimit = phourLimit;
        this.personLimit = pPersonLimits;
    }
}
