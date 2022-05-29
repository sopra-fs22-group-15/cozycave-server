package ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void getRole() {
        assertEquals(Role.LANDLORD, Role.getRole(1));
        assertEquals(Role.STUDENT, Role.getRole(2));
        assertEquals(Role.TEAM, Role.getRole(100));
        assertEquals(Role.ADMIN, Role.getRole(999));
        assertEquals(Role.INTERNAL, Role.getRole(1000));
        assertNull(Role.getRole(1337));
    }

    @Test
    void greaterEquals() {
        assertTrue(Role.ADMIN.greaterEquals(Role.STUDENT));
        assertTrue(Role.ADMIN.greaterEquals(Role.ADMIN));
        assertFalse(Role.STUDENT.greaterEquals(Role.TEAM));
    }

    @Test
    void greaterThan() {
        assertTrue(Role.ADMIN.greaterThan(Role.STUDENT));
        assertFalse(Role.ADMIN.greaterThan(Role.ADMIN));
        assertFalse(Role.STUDENT.greaterThan(Role.TEAM));
    }

    @Test
    void lessEquals() {
        assertTrue(Role.STUDENT.lessEquals(Role.ADMIN));
        assertTrue(Role.ADMIN.lessEquals(Role.ADMIN));
        assertFalse(Role.ADMIN.lessEquals(Role.STUDENT));
    }

    @Test
    void lessThan() {
        assertTrue(Role.STUDENT.lessThan(Role.ADMIN));
        assertFalse(Role.ADMIN.lessThan(Role.ADMIN));
        assertFalse(Role.STUDENT.lessThan(Role.LANDLORD));
    }

    @Test
    void generatePermittedAuthoritiesList() {
        assertEquals(4, Role.ADMIN.generatePermittedAuthoritiesList().size());
        assertEquals(3, Role.TEAM.generatePermittedAuthoritiesList().size());
        assertEquals(2, Role.STUDENT.generatePermittedAuthoritiesList().size());
        assertEquals(1, Role.LANDLORD.generatePermittedAuthoritiesList().size());
    }

    @Test
    void isTeam() {
        assertFalse(Role.STUDENT.isTeam());
        assertTrue(Role.ADMIN.isTeam());
    }

    @Test
    void isUser() {
        assertTrue(Role.STUDENT.isUser());
        assertFalse(Role.ADMIN.isUser());
    }
}