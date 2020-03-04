package mops.db.repositories;

import mops.db.dto.ApplicationDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends CrudRepository<ApplicationDTO, Long> {
}
