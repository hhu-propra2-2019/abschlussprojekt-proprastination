package mops.db.repositories;

import mops.db.dto.ApplicantDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ApplicantRepository extends CrudRepository<ApplicantDTO, String> {

    /**
     * Search by Username.
     *
     * @param username username.
     * @return JSON String.
     */
    @Query("SELECT * FROM applicant where username = :username;")
    ApplicantDTO findDistinctByUsername(@Param("username") String username);

    /**
     * Get id for username,
     *
     * @param username username.
     * @return int id;
     */
    @Query("SELECT id FROM applicant where username = :user")
    Optional<Integer> getIdByUsername(@Param("user") String username);

    /**
     * Returns all Applications as JSON-String list.
     *
     * @return List<String> , String in JSON format.
     */
    @Query("select y.x FROM applicant app, Lateral (select json_array_elements(details -> 'applications') as x)as y;")
    List<String> findAllApplications();

    /**
     * Returns all Applications machting @moduleName as JSON-String list.
     *
     * @param moduleName String of the modulename.
     * @return List<Application>
     */
    @Query("SELECT y.x FROM applicant, Lateral (SELECT json_array_elements(details -> 'applications')"
            + " AS x)AS y WHERE y.x->>'module' = :module;")
    List<String> findAllApplicationsByModuleName(@Param("module") String moduleName);


}
