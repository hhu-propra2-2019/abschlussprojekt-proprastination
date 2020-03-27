package mops.model.classes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void testBuilder() {
        Course course = Course.builder()
                .name("Daydreaming")
                .build();

        assertThat(course)
                .hasFieldOrPropertyWithValue("name", "Daydreaming");
    }

    @Test
    void testEquals() {
        Course course1 = Course.builder()
                .name("Daydreaming")
                .build();

        Course course2 = Course.builder()
                .name("Daydreaming")
                .build();

        assertEquals(course1, course2);
        assertEquals(course2, course1);
    }

    @Test
    void testEqualsNotEqual() {
        Course course1 = Course.builder()
                .name("Daydreaming")
                .build();

        Course course2 = Course.builder()
                .name("Hitchhiking")
                .build();

        assertNotEquals(course1, course2);
        assertNotEquals(course2, course1);
    }

    @Test
    void testHashCode() {
        Course course1 = Course.builder()
                .name("Daydreaming")
                .build();

        Course course2 = Course.builder()
                .name("Daydreaming")
                .build();

        int hashCode1 = course1.hashCode();
        int hashCode2 = course2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeNotEqual() {
        Course course1 = Course.builder()
                .name("Daydreaming")
                .build();

        Course course2 = Course.builder()
                .name("Hitchhiking")
                .build();

        int hashCode1 = course1.hashCode();
        int hashCode2 = course2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void testToString() {
        Course course = Course.builder()
                .name("Daydreaming")
                .build();
        String expected = "Course(name=Daydreaming)";

        String result = course.toString();

        assertEquals(expected, result);
    }

    @Test
    void testNoArgsConstructor() {
        Course emptyCourse = new Course();

        assertNull(emptyCourse.getName());
    }
}