package mops.model.classes;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Data
@Entity
@Table("MODULE")
public class Module {
    @Id
    private final long id;
    private final String name;
}
