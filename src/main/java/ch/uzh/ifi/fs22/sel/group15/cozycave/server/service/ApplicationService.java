package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ApplicationRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ListingRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service @Transactional public class ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    @Autowired public ApplicationService(@Qualifier("applicationRepository") ApplicationRepository applicationRepository, ListingRepository listingRepository, UserRepository userRepository) {
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
        newApplication.setApplication_status(ApplicationStatus.PENDING);
        newApplication.setId(UUID.randomUUID());

        Listing listing = listingRepository.getOne(newApplication.getListing().getId());

        if (listing.getAvailable() == false) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing not available for new applications");
        }

        User applicant = userRepository.getOne(newApplication.getApplicant().getId());

        if (!applicant.getRole().greaterEquals(Role.STUDENT)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Applications only allowed for Students");
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
        updatedApplication.setApplication_status(applicationInput.getApplication_status());

        return this.applicationRepository.saveAndFlush(updatedApplication);
    }

    public void deleteApplication(Application application) {
        //TODO: delete all applications once a user is deleted
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
        User publisher = userRepository.getOne(applicationInput.getListing().getPublisher().getId());
        if (publisher != decider
                && (!decider.getRole().greaterEquals(Role.ADMIN))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You're not the publisher of the listing to decide upon the application");
        }
        updatedApplication.setApplication_status(applicationInput.getApplication_status());

        // decline all other applications automatically
        if (updatedApplication.getApplication_status() == ApplicationStatus.ACCEPTED) {
            for (Application deniedApplication : applicationRepository.findByListing_Id(updatedApplication.getListing().getId())) {
                if (deniedApplication.getId() == updatedApplication.getId()) {
                    continue;
                }
                deniedApplication.setApplication_status(ApplicationStatus.DENIED);
                applicationRepository.saveAndFlush(deniedApplication);
            }
        }

        //TODO: Observer Pattern to notify subscriber

        // IDEAS: have a list of applications in the Listing
        // send notification to all applications by iterating through them applications

        return applicationRepository.saveAndFlush(updatedApplication);
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
                || applicationToBeCreated.getApplication_status() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "mandatory fields must be filled before publishing");
        }
        // check if application has even sensible values
        userRepository.findById(applicationToBeCreated.getApplicant().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "listing not found"));
        listingRepository.findById(applicationToBeCreated.getListing().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "listing not found"));
    }
}