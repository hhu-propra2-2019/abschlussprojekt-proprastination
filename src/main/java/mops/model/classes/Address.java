package mops.model.classes;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Data
@Entity
@Table("ADDRESS")
public class Address {
    @Id
    private final long id;
    private final String street;
    private final String houseNumber;
    private final String city;
    private final String country;
    private final int zipcode;
}
