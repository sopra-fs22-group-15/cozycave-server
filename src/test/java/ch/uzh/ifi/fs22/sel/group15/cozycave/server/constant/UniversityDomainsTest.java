package ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class UniversityDomainsTest {

    private UniversityDomains universityDomains;

    @BeforeAll
    void beforeAll() {
        universityDomains = new UniversityDomains();
    }

    @Test
    void getUniversities() {
        Assertions.assertTrue(universityDomains.getUniversities().size() > 0);
    }

    @Test
    void getUniversityByDomain() {
        Assertions.assertNotNull(universityDomains.getUniversityByDomain("uzh.ch"));
        Assertions.assertNull(universityDomains.getUniversityByDomain("test.uzh.ch"));
        Assertions.assertNull(universityDomains.getUniversityByDomain("google.com"));
    }

    @Test
    void getUniversityByEmail() {
        Assertions.assertNotNull(universityDomains.getUniversityByEmail("test@uzh.ch"));
        Assertions.assertNull(universityDomains.getUniversityByEmail("test@test.uzh.ch"));
        Assertions.assertNull(universityDomains.getUniversityByEmail("test@google.com"));
    }

    @Test
    void matchesDomain() {
        Assertions.assertTrue(universityDomains.matchesDomain("uzh.ch"));
        Assertions.assertFalse(universityDomains.matchesDomain("test.uzh.ch"));
        Assertions.assertFalse(universityDomains.matchesDomain("google.com"));
    }

    @Test
    void matchesEmail() {
        Assertions.assertTrue(universityDomains.matchesEmail("test@uzh.ch"));
        Assertions.assertFalse(universityDomains.matchesEmail("test@test.uzh.ch"));
        Assertions.assertFalse(universityDomains.matchesEmail("test@google.com"));
    }
}