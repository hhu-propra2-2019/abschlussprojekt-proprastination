package mops.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "id")
@Entity
@Table
@Builder(builderClassName = "ModuleBuilder", toBuilder = true)
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String shortName;
    private String profName;
    private String sevenHourLimit;
    private String nineHourLimit;
    private String seventeenHourLimit;
    private String hourLimit;

}
