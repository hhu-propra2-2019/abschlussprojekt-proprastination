package mops.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table("ADDRESS")
public class AddressDTO {

    private String street;
    private String city;
    private String country;
    private int zipcode;
}
