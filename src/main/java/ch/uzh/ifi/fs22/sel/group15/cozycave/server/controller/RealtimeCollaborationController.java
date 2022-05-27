package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetPublicDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/v1")
@Slf4j
public class RealtimeCollaborationController {

    private final UserService userService;

    RealtimeCollaborationController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/realtime")
    @SendTo("/collaboration/greetings")
    public UserGetPublicDto.UserDetailsDto subscribe(UserGetDto userGetDto) throws Exception {

        User userInput = UserMapper.INSTANCE.userGetDtoToUser(userGetDto);
        if (!userService.existsUser(userInput.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "error finding user");
        }

        UserGetPublicDto publicProfile = UserMapper.INSTANCE.userToUserGetPublicDto(userInput);

        // Delay for user so
        Thread.sleep(2000);
        return publicProfile.getDetails();
    }

    // TODO: approve specific user to get all details

}
