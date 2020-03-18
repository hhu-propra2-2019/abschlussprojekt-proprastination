package mops.model.classes;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.util.List;

@Builder
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
    private String module;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Applicant> employees;

}
