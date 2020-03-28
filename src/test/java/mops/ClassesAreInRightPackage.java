package mops;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ClassesAreInRightPackage {

 /*   @Test
    public void servicesAreInServices() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("mops");

        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Service")
                .should().resideInAPackage("mops.services.*");
        //.andshould().beAnnotatedWith(Service.class);

        rule.check(importedClasses);
    }*/

    @Test
    public void repositorysAreInRepositorys() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("mops");

        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("mops.repositories")
                .andShould().beAnnotatedWith(Repository.class);
        rule.check(importedClasses);
    }

    @Test
    public void configsAreInConfig() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("mops");

        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Config")
                .should().resideInAPackage("mops.config")
                .andShould().beAnnotatedWith(Configuration.class);
        rule.check(importedClasses);
    }

}