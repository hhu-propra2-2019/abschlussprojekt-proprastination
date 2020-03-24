package mops.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mops.model.classes.webclasses.WebModule;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;


@Setter
@Builder(toBuilder = true, builderClassName = "ModuleBuilder")
@EqualsAndHashCode
@Getter
@ToString(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private Instant deadline;
    private String shortName;
    private String profName;
    private String sevenHourLimit;
    private String nineHourLimit;
    private String seventeenHourLimit;
    private String hourLimit;
    /**
     * Transfer Module into WebModule
     * @return WebModule
     */
    public WebModule toWebModule() {
        WebModule m = WebModule.builder()
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
