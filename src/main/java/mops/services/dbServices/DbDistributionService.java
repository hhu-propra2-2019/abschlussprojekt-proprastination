package mops.services.dbServices;

import mops.model.classes.Distribution;
import mops.model.classes.Module;
import mops.repositories.DistributionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbDistributionService {

    private final DistributionRepository distributionRepository;

    /**
     * Constructor
     * @param distributionRepository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public DbDistributionService(final DistributionRepository distributionRepository) {
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

    /**
     * -
     */
    public void deleteAll() {
        distributionRepository.deleteAll();
    }

    /**
     * -
     * @param distribution
     */
    public void save(final Distribution distribution) {
        distributionRepository.save(distribution);
    }

    /**
     * -
     * @param id
     * @return -
     */
    public Optional<Distribution> findById(final long id) {
        return distributionRepository.findById(id);
    }

    /**
     * -
     * @return -
     */
    public long count() {
        return distributionRepository.count();
    }
}
