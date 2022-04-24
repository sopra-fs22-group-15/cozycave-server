package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

// TODO: add @JsonProperty annotations
public class UserPostDto implements Serializable {

    @JsonProperty("authentication")
    private AuthenticationDataDto authenticationData;
    private Role role;
    private UserDetailsDto details;

    public UserPostDto() {
    }

    public UserPostDto(
        AuthenticationDataDto authenticationData, Role role,
        UserDetailsDto details) {
        this.authenticationData = authenticationData;
        this.role = role;
        this.details = details;
    }

    public AuthenticationDataDto getAuthenticationData() {
        return authenticationData;
    }

    public void setAuthenticationData(
        AuthenticationDataDto authenticationData) {
        this.authenticationData = authenticationData;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserDetailsDto getDetails() {
        return details;
    }

    public void setDetails(UserDetailsDto details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPostDto entity = (UserPostDto) o;
        return Objects.equals(this.authenticationData, entity.authenticationData) &&
            Objects.equals(this.role, entity.role) &&
            Objects.equals(this.details, entity.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticationData, role, details);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "authenticationData = " + authenticationData + ", " +
            "role = " + role + ", " +
            "details = " + details + ")";
    }

    public static class AuthenticationDataDto implements Serializable {

        private String email;
        private String password;

        public AuthenticationDataDto() {
        }

        public AuthenticationDataDto(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AuthenticationDataDto entity = (AuthenticationDataDto) o;
            return Objects.equals(this.email, entity.email) &&
                Objects.equals(this.password, entity.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(email, password);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                "email = " + email + ", " +
                "password = " + password + ")";
        }
    }

    public static class UserDetailsDto implements Serializable {

        private String firstname;
        private String lastname;
        private Gender gender;
        private Date birthday;
        private LocationDto address;
        private String biography;

        public UserDetailsDto() {
        }

        public UserDetailsDto(String firstname, String lastname,
            Gender gender, Date birthday,
            LocationDto address, String biography) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.gender = gender;
            this.birthday = birthday;
            this.address = address;
            this.biography = biography;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public LocationDto getAddress() {
            return address;
        }

        public void setAddress(LocationDto address) {
            this.address = address;
        }

        public String getBiography() {
            return biography;
        }

        public void setBiography(String biography) {
            this.biography = biography;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            UserDetailsDto entity = (UserDetailsDto) o;
            return Objects.equals(this.firstname, entity.firstname) &&
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.gender, entity.gender) &&
                Objects.equals(this.birthday, entity.birthday) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.biography, entity.biography);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstname, lastname, gender, birthday, address, biography);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                "firstname = " + firstname + ", " +
                "lastname = " + lastname + ", " +
                "gender = " + gender + ", " +
                "birthday = " + birthday + ", " +
                "address = " + address + ", " +
                "biography = " + biography + ")";
        }
    }
}
