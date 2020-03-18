package mops.services;

import mops.model.classes.Course;
import mops.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Injects the Repository
     *
     * @param courseRepository the injected Repository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public CourseService(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Returns all Courses
     *
     * @return List of all courses
     */
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
