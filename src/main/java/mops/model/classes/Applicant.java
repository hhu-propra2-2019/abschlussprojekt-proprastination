package mops.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@Builder(toBuilder = true)
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
    @Singular
    private Set<Application> applications;
}
