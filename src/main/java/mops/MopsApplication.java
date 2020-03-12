package mops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
public class MopsApplication {

    /**
     * Starts the spring boot application.
     *
     * @param args Commandline parameters
     */
    public static void main(final String[] args) {
        SpringApplication.run(MopsApplication.class, args);
    }


}
