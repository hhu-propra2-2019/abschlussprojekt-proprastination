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

import javax.persistence.*;
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "applications_id")
    @Singular
    private Set<Application> applications;

    /**
     * Returns Application given the id.
     *
     * @param id long.
     * @return searched Application
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public Application getApplicationById(final long id) {
        for (Application a : applications) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }
}
