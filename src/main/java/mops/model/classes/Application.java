package mops.model.classes;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@Builder
@Entity
@Table("APPLICATION")
public class Application {
    @Id
    private final long id;
    private final int hours;
    @NonNull
    private final String module;
    private final int priority;
    private final double grade;
    private final String lecturer;
    private final String semester;
    private final String role;
}
