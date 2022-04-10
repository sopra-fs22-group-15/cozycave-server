package ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant;

import java.util.ArrayList;
import java.util.List;


public enum UniversityDomain {
    uzh, eth;

    private List<String> domains;

    //TODO: discuss how we should store university domains
    public void UniversityDomain() {
        this.domains = new ArrayList<>();
    }
}
