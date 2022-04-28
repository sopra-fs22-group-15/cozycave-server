package ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversityDomains {

    private static UniversityDomains instance;

    private HashSet<University> universities;

    private UniversityDomains() {
        instance = this;

        new Thread(() -> {
            universities = generateUniversities();
        }, "get university email addresses").start();
    }

    public static @NotNull UniversityDomains getInstance() {
        if (instance == null) {
            instance = new UniversityDomains();
        }

        return instance;
    }

    public ImmutableSet<University> getUniversities() {
        return ImmutableSet.copyOf(universities);
    }

    public @Nullable University getUniversityByDomain(String domain) {
        return universities.stream().filter(u -> u.matchesDomain(domain)).findFirst().orElse(null);
    }

    public @Nullable University getUniversityByEmail(String email) {
        return universities.stream().filter(u -> u.matchesEmail(email)).findFirst().orElse(null);
    }

    public boolean matchesDomain(String domain) {
        return universities.stream().anyMatch(u -> u.matchesDomain(domain));
    }

    public boolean matchesEmail(String email) {
        return universities.stream().anyMatch(u -> u.matchesEmail(email));
    }

    private @NotNull HashSet<University> generateUniversities() {
        final Logger logger = LoggerFactory.getLogger(UniversityDomains.class);

        HashSet<University> universities = new HashSet<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("http://universities.hipolabs.com/search?country=Switzerland"))
            .timeout(Duration.ofSeconds(10))
            .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            logger.error("Could not get university domains!", e);
            e.printStackTrace();
        }

        assert response != null;
        assert response.body() != null;
        assert !response.body().equals("");

        JsonArray r = JsonParser.parseString(response.body()).getAsJsonArray();

        if (response.statusCode() != 200) {
            logger.error("Could not get university domains! Status code: " + response.statusCode());
            return universities;
        }

        logger.debug("Received university domains!");

        for (JsonElement jsonElement : r) {
            JsonObject o = jsonElement.getAsJsonObject();

            String name = o.get("name").getAsString();
            JsonArray jsonDomains = o.get("domains").getAsJsonArray();

            HashSet<String> domains = new HashSet<>();
            jsonDomains.forEach(d -> domains.add(d.getAsString().toLowerCase()));

            Locale.IsoCountryCode country = Locale.IsoCountryCode.valueOf(
                o.get("country").getAsString().toUpperCase());

            universities.add(new University(name, country, ImmutableSet.copyOf(domains)));
        }

        return universities;
    }

    private record University(
        @NotNull String name,
        @NotNull Locale.IsoCountryCode county,
        @NotNull ImmutableSet<String> domains
    ) {

        private University {
            domains = domains.stream().map(String::toLowerCase).collect(ImmutableSet.toImmutableSet());
        }

        public boolean matchesDomain(String domain) {
            return domains.contains(domain);
        }

        public boolean matchesEmail(String email) {
            return domains.stream().anyMatch(d -> email.toLowerCase().endsWith("@" + d));
        }

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof University)) {
                return false;
            }
            University that = (University) o;
            return name.equals(that.name) && county == that.county && domains.equals(that.domains);
        }

        @Override public int hashCode() {
            return Objects.hash(name, county, domains);
        }

        @Override public String toString() {
            return "University{" +
                "name='" + name + '\'' +
                ", county=" + county +
                ", domains=" + domains +
                '}';
        }
    }
}
