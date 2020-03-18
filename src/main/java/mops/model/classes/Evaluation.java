package mops.model.classes;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;

@Builder
@EqualsAndHashCode
@Getter
@ToString(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Application application;
    private String comment;
    private int priority;
}
