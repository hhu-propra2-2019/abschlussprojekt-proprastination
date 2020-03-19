package mops.repositories;

import mops.model.classes.Distribution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistributionRepository extends CrudRepository<Distribution, Long> {

    @Override
    List<Distribution> findAll();

    @Override
    Optional<Distribution> findById(Long id);

    /**
     * Finds the Distribution of a model
     *
     * @param module the model
     * @return List of Distributions
     */
    Distribution findByModule(String module);

    @Override
    Distribution save(Distribution distribution);


}
