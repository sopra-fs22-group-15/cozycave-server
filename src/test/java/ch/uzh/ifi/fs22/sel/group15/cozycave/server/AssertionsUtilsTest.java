package ch.uzh.ifi.fs22.sel.group15.cozycave.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssertionsUtilsTest {

    @Test
    void isEmpty() {
        Assertions.assertTrue(AssertionsUtils.isEmpty(List.of()));
        Assertions.assertFalse(AssertionsUtils.isEmpty(List.of("")));
    }

    @Test
    void testIsEmpty() {
        Assertions.assertFalse(AssertionsUtils.isEmpty(List.of("").toArray()));
        Assertions.assertTrue(AssertionsUtils.isEmpty(new String[0]));
    }

    @Test
    void testIsEmpty1() {
        Assertions.assertTrue(AssertionsUtils.isEmpty(new HashMap<>()));
        Assertions.assertFalse(AssertionsUtils.isEmpty(Map.of("A", "B")));
    }
}