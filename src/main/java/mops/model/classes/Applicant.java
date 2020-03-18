package mops.model.classes;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@EqualsAndHashCode
@Getter
@ToString(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uniserial;
    private String birthplace;
    private String title;
    private String firstName;

    private String surname;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;
    private String gender;
    private String birthday;
    private String nationality;
    private String course;
    private String status;
    private String comment;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Certificate certs;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Application> applications;
}
