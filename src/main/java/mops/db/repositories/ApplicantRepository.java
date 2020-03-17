package mops.db.repositories;

import mops.db.dto.ApplicantDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ApplicantRepository extends CrudRepository<ApplicantDTO, Long> {

    /**
     * Search by Username.
     *
     * @param username username.
     * @return JSON String.
     */
    @Query("SELECT * FROM applicant where uniserial = :username;")
    ApplicantDTO findDistinctByUsername(@Param("username") String username);

    /**
     * Get id for username,
     *
     * @param username username.
     * @return int id;
     */
    @Query("SELECT id FROM applicant where uniserial = :user")
    Optional<Long> getIdByUsername(@Param("user") String username);


}
