package mops.model.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Certificate {
    private final String name;
    private final String university;
}
