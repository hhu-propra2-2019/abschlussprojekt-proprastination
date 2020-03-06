package mops;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitTests {

    @Test
    public void some_architecture_rule() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("mops");

        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Service")
                .should().resideInAPackage("mops.services");
        //.andshould().beAnnotatedWith(Service.class);

        rule.check(importedClasses);
    }

}