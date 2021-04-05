package pl.mjaskola.app;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("pl.mjaskola.app");

        noClasses()
            .that()
            .resideInAnyPackage("pl.mjaskola.app.service..")
            .or()
            .resideInAnyPackage("pl.mjaskola.app.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..pl.mjaskola.app.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
