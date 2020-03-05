package mops.db.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
@SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "not gonna change Lombok")
@EqualsAndHashCode
public class ApplicationDTO {
    @SuppressFBWarnings(value = "SS_SHOULD_BE_STATIC", justification = "no, it shouldn't be static")
    private final String name = "empty";
    @Id
    private long id;
}
