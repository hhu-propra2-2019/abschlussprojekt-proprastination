package mops;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "mops")
public class LayerTests {

    @ArchTest
    static final ArchRule servicesShouldNotAccessControllers =
            noClasses()
                    .that().resideInAPackage("..service..")
                    .should().accessClassesThat().resideInAPackage("..controller..");

    @ArchTest
    static final ArchRule persistenceShouldNotAccessServices =
            noClasses()
                    .that().resideInAPackage("..persistence..")
                    .should().accessClassesThat().resideInAPackage("..service..");

    @ArchTest
    static final ArchRule servicesShouldOnlyBeAccessedByControllersOrOtherServices =
            classes()
                    .that().resideInAPackage("..service..")
                    .should().onlyBeAccessed().byAnyPackage("..controller..", "..service..");

    @ArchTest
    static final ArchRule servicesShouldOnlyAccessPersistenceOrOtherServices =
            classes()
                    .that().resideInAPackage("..service..")
                    .should().onlyAccessClassesThat().resideInAnyPackage("..service..", "..persistence..", "java..");

    @ArchTest
    static final ArchRule servicesShouldNotDependOnControllers =
            noClasses()
                    .that().resideInAPackage("..service..")
                    .should().dependOnClassesThat().resideInAPackage("..controller..");

    @ArchTest
    static final ArchRule persistenceShouldNotDependOnServices =
            noClasses()
                    .that().resideInAPackage("..persistence..")
                    .should().dependOnClassesThat().resideInAPackage("..service..");

    @ArchTest
    static final ArchRule servicesShouldOnlyBeDependedOnByControllersOrOtherServices =
            classes()
                    .that().resideInAPackage("..service..")
                    .should().onlyHaveDependentClassesThat().resideInAnyPackage("..controller..", "..service..");

    @ArchTest
    static final ArchRule servicesShouldOnlyDependOnPersistenceOrOtherServices =
            classes()
                    .that().resideInAPackage("..service..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..service..", "..persistence..", "java..", "javax..");

}
