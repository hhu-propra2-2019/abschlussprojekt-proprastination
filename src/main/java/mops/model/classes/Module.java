package mops.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


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
    private String applicantDeadlineDate;
    private String applicantDeadlineTime;
    private LocalDateTime applicantDeadline;
    private String orgaDeadlineDate;
    private String orgaDeadlineTime;
    private LocalDateTime orgaDeadline;
    private String shortName;
    private String profSerial;
    private String sevenHourLimit;
    private String nineHourLimit;
    private String seventeenHourLimit;
}
