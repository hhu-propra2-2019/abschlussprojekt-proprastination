package mops.db.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table("CERTIFICATE")
public class CertificateDTO {
    private String name;
    private String course;
}
