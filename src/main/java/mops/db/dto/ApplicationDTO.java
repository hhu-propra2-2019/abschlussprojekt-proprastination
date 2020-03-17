package mops.db.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import mops.model.classes.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table("APPLICATION")
public class ApplicationDTO {

    @Id
    private Long id;
    private int hours;
    private String module;
    private int priority;
    private double grade;
    private String lecturer;
    private String semester;
    private Role role;
}
