package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class UserGetDto implements Serializable {

    private UUID id;
    @JsonProperty("creation_date")
    private Date creationDate;
    @JsonProperty("authentication")
    private AuthenticationDataDto authenticationData;
    private Role role;
    private UserDetailsDto details;

    public UserGetDto() {
    }

    public UserGetDto(UUID id, Date creationDate,
        AuthenticationDataDto authenticationData, Role role,
        UserDetailsDto details) {
        this.id = id;
        this.creationDate = creationDate;
        this.authenticationData = authenticationData;
        this.role = role;
        this.details = details;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public AuthenticationDataDto getAuthenticationData() {
        return authenticationData;
    }

    public void setAuthenticationData(AuthenticationDataDto authenticationData) {
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
        UserGetDto entity = (UserGetDto) o;
        return Objects.equals(this.id, entity.id) &&
            Objects.equals(this.creationDate, entity.creationDate) &&
            Objects.equals(this.authenticationData, entity.authenticationData) &&
            Objects.equals(this.role, entity.role) &&
            Objects.equals(this.details, entity.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, authenticationData, role, details);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "creationDate = " + creationDate + ", " +
            "authenticationData = " + authenticationData + ", " +
            "role = " + role + ", " +
            "details = " + details + ")";
    }

    public static class AuthenticationDataDto implements Serializable {

        private String email;
        private String token;

        public AuthenticationDataDto() {
        }

        public AuthenticationDataDto(String email) {
            this.email = email;
            this.token = null;
        }

        public AuthenticationDataDto(String email, String token) {
            this.email = email;
            this.token = token;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class UserDetailsDto implements Serializable {

        private String firstname;
        private String lastname;
        private Gender gender;
        private Date birthday;
        private LocationDto address;
        private String biography;
        private PictureDto picture;

        public UserDetailsDto() {
        }

        public UserDetailsDto(String firstname, String lastname,
            Gender gender, Date birthday,
            LocationDto address, String biography,
            PictureDto picture) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.gender = gender;
            this.birthday = birthday;
            this.address = address;
            this.biography = biography;
            this.picture = picture;
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

        public PictureDto getPicture() {
            return picture;
        }

        public void setPicture(PictureDto picture) {
            this.picture = picture;
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
                Objects.equals(this.biography, entity.biography) &&
                Objects.equals(this.picture, entity.picture);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstname, lastname, gender, birthday, address, biography, picture);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                "firstname = " + firstname + ", " +
                "lastname = " + lastname + ", " +
                "gender = " + gender + ", " +
                "birthday = " + birthday + ", " +
                "address = " + address + ", " +
                "biography = " + biography + ", " +
                "picture = " + picture + ")";
        }

        public static class PictureDto implements Serializable {

            private UUID id;
            private UUID uploaderId;

            public PictureDto() {
            }

            public PictureDto(UUID id, UUID uploaderId) {
                this.id = id;
                this.uploaderId = uploaderId;
            }

            public UUID getId() {
                return id;
            }

            public void setId(UUID id) {
                this.id = id;
            }

            public UUID getUploaderId() {
                return uploaderId;
            }

            public void setUploaderId(UUID uploaderId) {
                this.uploaderId = uploaderId;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                PictureDto entity = (PictureDto) o;
                return Objects.equals(this.id, entity.id) &&
                    Objects.equals(this.uploaderId, entity.uploaderId);
            }

            @Override
            public int hashCode() {
                return Objects.hash(id, uploaderId);
            }

            @Override
            public String toString() {
                return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "uploaderId = " + uploaderId + ")";
            }
        }
    }
}
