package mops.services.dbServices;

import mops.model.classes.Distribution;
import mops.model.classes.Module;
import mops.repositories.DistributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbDistributionService {

    private final DistributionRepository distributionRepository;

    public DbDistributionService(DistributionRepository distributionRepository) {
        this.distributionRepository = distributionRepository;
    }

    /**
     * Finds the Distribution of a model
     *
     * @param module the model
     * @return List of Distributions
     */
    public Distribution findByModule(final Module module) {
        return distributionRepository.findByModule(module);
    }

    /**
     * Finds all Distributions
     *
     * @return List of Distributions
     */
    public List<Distribution> findAll() {
        return distributionRepository.findAll();
    }

    public void deleteAll() {
        distributionRepository.deleteAll();
    }

    public void save(Distribution distribution) {
        distributionRepository.save(distribution);
    }

    public Optional<Distribution> findById(long id) {
        return distributionRepository.findById(id);
    }

    public long count() {
        return distributionRepository.count();
    }
}
