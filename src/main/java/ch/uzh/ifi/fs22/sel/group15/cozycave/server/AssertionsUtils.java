package ch.uzh.ifi.fs22.sel.group15.cozycave.server;

import java.util.Map;

public class AssertionsUtils {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isEmpty(Iterable<?> i) {
        return i == null || !i.iterator().hasNext();
    }

    public static boolean isEmpty(Object[] o) {
        return o == null || o.length == 0;
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }
}
