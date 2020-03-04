package mops.services;

import mops.db.dto.ApplicationDTO;
import mops.db.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    /**
     * Application Repository.
     */
    private ApplicationRepository applicationRepo;

    /**
     * Autowires Application Repository.
     *
     * @param applicationRepo Autowired Repo.
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public ApplicationServiceImpl(final ApplicationRepository applicationRepo) {
        this.applicationRepo = applicationRepo;
    }

    /**
     * Returns all ApplicationDTOs as List.
     *
     * @return List<ApplicationDTO>
     */
    public List<ApplicationDTO> getAll() {
        List<ApplicationDTO> list = new ArrayList<>();
        applicationRepo.findAll().forEach(list::add);
        return list;
    }

}

