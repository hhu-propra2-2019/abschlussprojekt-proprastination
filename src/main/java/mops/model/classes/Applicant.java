package mops.model.classes;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Builder
@Data
@Entity
@Table("APPLICANT")
public class Applicant {
    @Id
    private final long id;
    private final String uniserial;
    private final String surename;
    private final String name;
    private final String birthplace;
    private final Address address;
    private final String birthday;
    private final String nationality;
    private final String course;
    private final String status;
    private final String comment;
    private final Certificate certs;
    @Singular
    private final List<Application> applications;
}
