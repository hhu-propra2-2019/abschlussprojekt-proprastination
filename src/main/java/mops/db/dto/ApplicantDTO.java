package mops.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.model.classes.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table("APPLICANT")
public class ApplicantDTO {

    @Id
    private Long id;
    private String uniserial;
    private String surname;
    private String name;
    private String birthplace;

    @MappedCollection(idColumn = "applicant")
    private AddressDTO address;

    private String birthday;
    private String nationality;
    private String course;
    private Status status;
    private String comment;

    @MappedCollection(idColumn = "applicant")
    private CertificateDTO certificate;

    @MappedCollection(idColumn = "applicant", keyColumn = "id")
    private Set<ApplicationDTO> applications;
}
