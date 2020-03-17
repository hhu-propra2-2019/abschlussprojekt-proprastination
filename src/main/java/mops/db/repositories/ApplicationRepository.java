package mops.db.repositories;

import mops.db.dto.ApplicationDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<ApplicationDTO, Long> {

    /**
     * Returns all ApplicationDTOs for a given module.
     *
     * @param module
     * @return
     */
    @Query("SELECT * from application where module = :module")
    List<ApplicationDTO> findAllByModule(@Param("module") String module);

}
