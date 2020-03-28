package mops.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Builder(toBuilder = true)
@EqualsAndHashCode
@Getter
@ToString(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Distribution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "module_id")
    private Module module;
    @OneToMany(fetch = FetchType.EAGER)
    @Singular
    @JoinColumn(name = "applicant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Applicant> employees;

}
