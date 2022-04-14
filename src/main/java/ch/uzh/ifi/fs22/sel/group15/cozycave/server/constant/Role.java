package ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant;

import org.jetbrains.annotations.Nullable;

public enum Role {
    LANDLORD(1),
    STUDENT(2),
    TEAM(100),
    ADMIN(999);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public static @Nullable Role getRole(int id) {
        for (Role rank : Role.values()) {
            if (rank.getRoleId() == id) {
                return rank;
            }
        }

        return null;
    }

    public int getRoleId() {
        return id;
    }

    public boolean greaterEquals(Role rank) {
        return this.id >= rank.id;
    }

    public boolean greaterThan(Role rank) {
        return this.id > rank.getRoleId();
    }

    public boolean lessEquals(Role rank) {
        return this.id <= rank.getRoleId();
    }

    public boolean lessThan(Role rank) {
        return this.id < rank.getRoleId();
    }
}
