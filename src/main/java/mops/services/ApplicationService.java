package mops.services;

import mops.db.dto.ApplicationDTO;

import java.util.List;

public interface ApplicationService {

    /**
     * Returns All Applications
     *
     * @return List<ApplicationDTO>
     */
    List<ApplicationDTO> getAll();
}
