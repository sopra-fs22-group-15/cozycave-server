package ch.uzh.ifi.fs22.sel.group15.cozycave.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void generateSalt() {
        assertEquals(16, Utils.generateSalt(16).length());
        assertEquals(3, Utils.generateSalt(3).length());
    }

    @Test
    void stripNames() {
        assertEquals("Erika Mustermann", Utils.stripNames("  Erika Mustermann   "));
    }

    @Test
    void stripPhoneNumber() {
        assertEquals("+411234567", Utils.stripPhoneNumber("+41 123 45 67"));
        assertEquals("+411234567", Utils.stripPhoneNumber("+41 123-45-67"));
    }

    @Test
    void checkValidEmail() {
        assertTrue(Utils.checkValidEmail("dasd@dasd.dse.dea.de"));
        assertFalse(Utils.checkValidEmail("dasd@dasd.d@se.dea.de"));
    }
}