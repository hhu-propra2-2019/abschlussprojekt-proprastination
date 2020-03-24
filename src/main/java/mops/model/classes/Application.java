package mops.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder(toBuilder = true)
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
    private int minHours;
    private int finalHours;
    private int maxHours;
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "module_id")
    private Module module;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private double grade;
    private String lecturer;
    private String semester;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String comment;
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Applicant applicant;
}


