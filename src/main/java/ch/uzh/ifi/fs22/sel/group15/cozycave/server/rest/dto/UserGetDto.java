package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

// TODO: add @JsonProperty annotations
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

    public Date getCreationDate() {
        return creationDate;
    }

    public AuthenticationDataDto getAuthenticationData() {
        return authenticationData;
    }

    public Role getRole() {
        return role;
    }

    public UserDetailsDto getDetails() {
        return details;
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
        private String salt;

        public AuthenticationDataDto() {
        }

        public AuthenticationDataDto(String email, String salt) {
            this.email = email;
            this.salt = salt;
        }

        public String getEmail() {
            return email;
        }

        public String getSalt() {
            return salt;
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
                Objects.equals(this.salt, entity.salt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(email, salt);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                "email = " + email + ", " +
                "salt = " + salt + ")";
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

        public String getLastname() {
            return lastname;
        }

        public Gender getGender() {
            return gender;
        }

        public Date getBirthday() {
            return birthday;
        }

        public LocationDto getAddress() {
            return address;
        }

        public String getBiography() {
            return biography;
        }

        public PictureDto getPicture() {
            return picture;
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

            private final UUID id;
            private final UUID uploaderId;

            public PictureDto(UUID id, UUID uploaderId) {
                this.id = id;
                this.uploaderId = uploaderId;
            }

            public UUID getId() {
                return id;
            }

            public UUID getUploaderId() {
                return uploaderId;
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
