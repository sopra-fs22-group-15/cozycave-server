package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ApplicationRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ListingRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationService(@Qualifier("applicationRepository") ApplicationRepository applicationRepository, ListingRepository listingRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    // get all Applications of a user > id = user id
    public List<Application> findApplicationsOfUser(UUID id) {
        return this.applicationRepository.findByApplicant_Id(id);
    }

    // get all Applications of a user > id = user id
    public List<Application> findApplicationsOfUser(User user) {
        return this.applicationRepository.findByApplicant_Id(user.getId());
    }

    // get all Applications for a listing > id = listing id
    public List<Application> findApplicationsToListing(UUID id) {
        return this.applicationRepository.findByListing_Id(id);
    }

    // get all Applications for a listing > id = listing id
    public List<Application> findApplicationsToListing(Listing listing) {
        return this.applicationRepository.findByListing_Id(listing.getId());
    }

    // get concrete application > id = application id
    public Optional<Application> findApplicationById(UUID id) {
        return this.applicationRepository.findById(id);
    }

    // get concrete application > id = application id
    public Optional<Application> findApplicationById(Application application) {
        return this.applicationRepository.findById(application.getId());
    }

    public @NotNull Application createApplication(Application newApplication) {
        log.debug("create Application {}", newApplication);
        checkIfDataIsValid(newApplication);

        newApplication.setId(UUID.randomUUID());
        newApplication.setCreationDate(new Date());
        // manually set application to pending regardless of how application was sent
        newApplication.setApplicationStatus(ApplicationStatus.PENDING);
        newApplication.setId(UUID.randomUUID());

        try {
            User applicant = userRepository.getOne(newApplication.getApplicant().getId());
            if (!applicant.getRole().greaterEquals(Role.STUDENT)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "only students can apply to a listing");
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing not available for new applications");
        }

        try {
            Listing listing = listingRepository.getOne(newApplication.getListing().getId());

            if (listing.getAvailable() == false) {
                log.debug("listing with id: {} not available anymore", listing.getId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Listing not available for new applications");
            }
            if (listing.getPublished() == false) {
                log.debug("listing with id: {} not published", listing.getId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Listing not available for new applications");
            }
            if (!listing.getAvailableTo().contains(getGenderOfApplicant(newApplication))) {
                log.debug("listing with id: {} not available for gender: {}", listing.getId(), getGenderOfApplicant(newApplication));
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Listing not available for this gender");
            }
        } catch (EntityNotFoundException e) {
            log.debug("listing with id: {} not found", newApplication);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing not available for new applications");
        }

        // throw an error when there's already another application open to that listing
        for (Application openApplication : findApplicationsOfUser(newApplication.getApplicant())) {
            if (openApplication.getListing().getId() == newApplication.getListing().getId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "There is already an application open");
            }
        }

        newApplication = applicationRepository.saveAndFlush(newApplication);
        log.debug("Created new Application: {}", newApplication);

        return newApplication;
    }


    public @NotNull Application updateApplication(Application applicationInput) {
        log.debug("updating Application {}", applicationInput);
        checkIfDataIsValid(applicationInput);

        Application updatedApplication = applicationRepository.findById(applicationInput.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "listing not found"));
        updatedApplication.setApplicant(applicationInput.getApplicant());
        updatedApplication.setListing(applicationInput.getListing());
        updatedApplication.setApplicationStatus(applicationInput.getApplicationStatus());

        return this.applicationRepository.saveAndFlush(updatedApplication);
    }

    public void deleteApplication(Application application) {
        log.debug("delete Application {}", application);
        this.applicationRepository.delete(application);
    }

    public void deleteApplication(UUID id) {
        log.debug("delete Application {}", id);
        this.applicationRepository.deleteById(id);
    }

    public Application decideOnApplication(Application applicationInput, User decider) {
        log.debug("decide on Application {}", applicationInput);

        checkIfDataIsValid(applicationInput);

        Application updatedApplication = applicationRepository.findById(applicationInput.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "listing not found"));

        // only the publisher of the
        try {
            User publisher = userRepository.getOne(applicationInput.getListing().getPublisher().getId());
            if (publisher != decider
                    && (!decider.getRole().greaterEquals(Role.ADMIN))) {
                log.debug("publisher of listing is not the one who can decide, nor is he admin publisher id: {} decider id: {}", publisher.getId(), decider.getId());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "You're not the publisher of the listing to decide upon the application");
            }
        } catch (EntityNotFoundException e) {
            log.debug("publisher not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing not available for new applications");
        }

        updatedApplication.setApplicationStatus(applicationInput.getApplicationStatus());

        // decline all other applications automatically
        if (updatedApplication.getApplicationStatus() == ApplicationStatus.ACCEPTED) {
            for (Application deniedApplication : applicationRepository.findByListing_Id(updatedApplication.getListing().getId())) {
                if (deniedApplication.getId() == updatedApplication.getId()) {
                    continue;
                }
                deniedApplication.setApplicationStatus(ApplicationStatus.DENIED);
                applicationRepository.saveAndFlush(deniedApplication);
            }
        }

        //TODO: Observer Pattern to notify subscriber

        // IDEAS: have a list of applications in the Listing
        // send notification to all applications by iterating through them applications

        return applicationRepository.saveAndFlush(updatedApplication);
    }

    private Gender getGenderOfApplicant(Application application) {
        return application.getApplicant().getDetails().getGender();
    }

    private String getEmailOfApplicant(Application application) {
        return application.getApplicant().getAuthenticationData().getEmail();
    }


    private String getEmailOfPublisher(Application application) {
        return application.getListing().getPublisher().getAuthenticationData().getEmail();
    }

    private void checkIfDataIsValid(Application applicationToBeCreated) {
        // check if application has empty fiels
        if (applicationToBeCreated.getApplicant() == null
                || applicationToBeCreated.getListing() == null
                || applicationToBeCreated.getApplicationStatus() == null) {
            log.debug("values are not properly filled, applicant: {}, listing: {}, application_Status: {} ",
                    applicationToBeCreated.getApplicant(), applicationToBeCreated.getListing(), applicationToBeCreated.getApplicationStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "mandatory fields must be filled before publishing");
        }
        // check if application has even sensible values
        userRepository.findById(applicationToBeCreated.getApplicant().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        listingRepository.findById(applicationToBeCreated.getListing().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "listing not found"));
    }
}