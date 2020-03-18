package mops.model.classes;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@EqualsAndHashCode
@Getter
@ToString(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int hours;
    @NonNull
    private String module;
    private int priority;
    private double grade;
    private String lecturer;
    private String semester;
    private String role;
    private String comment;

}


