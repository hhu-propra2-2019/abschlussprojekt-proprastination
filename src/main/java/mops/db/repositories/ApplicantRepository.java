package mops.db.repositories;

import mops.db.dto.ApplicantDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends CrudRepository<ApplicantDTO, Long> {
}
