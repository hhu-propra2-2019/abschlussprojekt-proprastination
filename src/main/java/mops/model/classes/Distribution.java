package mops.model.classes;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Builder
@Data
@Entity
@Table("DISTRIBUTION")
public class Distribution {
    @Id
    private final long id;
    private final String module;
    private final List<Applicant> employees;
}
