package mops.repositories;

import mops.model.classes.Distribution;
import mops.model.classes.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistributionRepository extends CrudRepository<Distribution, Long> {

    @Override
    @NonNull
    List<Distribution> findAll();

    @Override
    @NonNull
    Optional<Distribution> findById(@NonNull Long id);

    /**
     * Finds the Distribution of a model
     *
     * @param module the model
     * @return List of Distributions
     */
    Distribution findByModule(Module module);

    //@Override
    //void save(Distribution distribution);


}
