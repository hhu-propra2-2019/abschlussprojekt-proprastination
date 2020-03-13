package mops.db.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "not gonna change Lombok")
@Table("applicant")
public class ApplicantDTO {
    //@SuppressFBWarnings(value = "SS_SHOULD_BE_STATIC", justification = "no, it shouldn't be static")
    @Id
    private int id;
    @Column("username")
    private String username;
    @Column("details")
    private String details;
}
