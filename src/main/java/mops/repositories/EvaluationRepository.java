package mops.repositories;

import mops.model.classes.Application;
import mops.model.classes.Evaluation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends CrudRepository<Evaluation, Long> {

    /**
     * Finds Application by evaluation
     * @param application the application
     * @return the evaluation found
     */
    Evaluation findByApplication(Application application);
}
