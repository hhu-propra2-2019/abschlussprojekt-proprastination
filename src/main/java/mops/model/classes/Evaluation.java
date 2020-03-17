package mops.model.classes;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@Entity
@Table("EVALUATION")
public class Evaluation {
    @Id
    private final long id;
    private final Application application;
    private final Applicant applicant;
    private final String comment;
    private final int priority;
}
