package mops.db.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
@EqualsAndHashCode
public class ApplicationDTO {
    /**
     * Application username (FINAL);
     */
    private final String name = "empty";
    /**
     * Application DTO-ID.
     */
    @Id
    private long id;
}
