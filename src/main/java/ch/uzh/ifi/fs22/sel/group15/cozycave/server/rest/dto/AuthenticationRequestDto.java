package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationRequestDto {

    private String email;
    private String password;
}
