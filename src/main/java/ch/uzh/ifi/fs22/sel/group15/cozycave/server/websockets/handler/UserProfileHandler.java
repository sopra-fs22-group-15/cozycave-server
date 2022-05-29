package ch.uzh.ifi.fs22.sel.group15.cozycave.server.websockets.handler;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetGTDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetGTPublicDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
@Slf4j
public class UserProfileHandler extends TextWebSocketHandler {

    //Map<UUID, WebSocketSession> webSocketSessions = Collections.synchronizedMap(new HashMap<>());
    private final HashBiMap<UUID, WebSocketSession> webSocketSessions = HashBiMap.create();
    private final Cache<UUID, UUID> requestCache = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        // check auth and add to list
        String authToken = getJwtFromRequest(session);

        if (!StringUtils.hasText(authToken) || !jwtTokenProvider.validateToken(authToken)) {
            session.sendMessage(Action.ERROR_UNAUTHORIZED.getTextMessage());
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        UUID uuid = jwtTokenProvider.getUuidFromToken(authToken);

        if (webSocketSessions.containsKey(uuid)) {
            session.sendMessage(Action.ERROR_ALREADY_CONNECTED.getTextMessage());
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        Role role = getRoleOfUser(uuid);
        if (role == null || role.lessThan(Role.STUDENT)) {
            session.sendMessage(Action.ERROR_FORBIDDEN.getTextMessage());
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        // send new joined user info to every subscriber
        sendUserJoinedToEveryone(uuid);

        // send registered users to registering user
        ArrayList<User> usersOnline = new ArrayList<>();

        for (UUID id : webSocketSessions.keySet()) {
            Optional<User> oU = userService.findUserID(id);

            oU.ifPresent(usersOnline::add);
        }

        session.sendMessage(Action.JOINED_ALL_USERS.getTextMessageWithData(new Gson().toJsonTree(
            usersOnline.stream()
                .map(UserMapper.INSTANCE::userToUserGetGTPublicDto)
                .toList()
        )));

        webSocketSessions.put(uuid, session);
    }

    private @Nullable String getJwtFromRequest(WebSocketSession session) {
        if (!session.getHandshakeHeaders().containsKey("Authorization")
            && (session.getUri() == null
            || session.getUri().getQuery() == null
            || session.getUri().getQuery().equalsIgnoreCase("token"))) {
            return null;
        }

        if (session.getHandshakeHeaders().containsKey("Authorization")) {
            String bearerToken = session.getHandshakeHeaders().getFirst("Authorization");

            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }

            return null;
        }

        return session.getUri().getQuery()
            .substring(session.getUri().getQuery().indexOf("token=") + 6)
            .split("&")[0];
    }

    private @Nullable Role getRoleOfUser(UUID uuid) {
        Optional<User> user = userService.findUserID(uuid);

        if (user.isEmpty()) {
            return null;
        }

        return user.get().getRole();
    }

    private void sendUserJoinedToEveryone(UUID uuid) throws Exception {
        Optional<User> optionalUser = userService.findUserID(uuid);

        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();

        UserGetGTPublicDto publicProfile = UserMapper.INSTANCE.userToUserGetGTPublicDto(user);

        for (WebSocketSession webSocketSession : webSocketSessions.values()) {
            webSocketSession.sendMessage(Action.NEW_USER.getTextMessageWithData(new Gson().toJsonTree(publicProfile)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        JsonObject uuidJson = new JsonObject();
        uuidJson.addProperty("uuid", String.valueOf(webSocketSessions.inverse().get(session)));

        webSocketSessions.inverse().remove(session);

        webSocketSessions.values().forEach(s -> {
            try {
                s.sendMessage(new TextMessage(Action.REMOVE_USER.getJsonWithData(uuidJson)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        // TODO: if uuid in hashmap as key, ask for permission and map new profile

        // Request Information
        if (!validateRequestPayload(message.getPayload())) {
            session.sendMessage(Action.ERROR_INVALID_DATA.getTextMessage());
            return;
        }

        JsonObject json = getJsonFromMessage(message.getPayload());

        if (json == null || !json.has("action_id")) {
            session.sendMessage(Action.ERROR_INVALID_DATA.getTextMessage());
            return;
        }

        Action action = Action.getFrom(json.get("action_id").getAsInt());
        UUID userUuid = webSocketSessions.inverse().get(session);

        switch (action) {
            // data: {uuid: "<UUID>"}
            // return: REQUESTED_FROM_USER.data: <UserGetGTPublicDto>
            case REQUEST_USER -> {
                if (!json.getAsJsonObject("data").has("uuid")) {
                    session.sendMessage(Action.ERROR_UNKNOWN_UUID.getTextMessage());
                    return;
                }

                UUID requestedUserUuid = UUID.fromString(json.getAsJsonObject("data").get("uuid").getAsString());

                if (requestCache.getIfPresent(userUuid) != null
                    && requestCache.getIfPresent(userUuid).equals(requestedUserUuid)) {
                    session.sendMessage(Action.ERROR_ALREADY_REQUESTED.getTextMessage());
                    return;
                }

                if (!webSocketSessions.containsKey(requestedUserUuid)) {
                    session.sendMessage(Action.ERROR_UNKNOWN_UUID.getTextMessage());
                    return;
                }

                requestCache.put(userUuid, requestedUserUuid);

                Optional<User> optionalUser = userService.findUserID(userUuid);
                if (optionalUser.isEmpty()) {
                    session.sendMessage(Action.ERROR_INTERNAL_SERVER_ERROR.getTextMessage());
                    return;
                }

                User user = optionalUser.get();

                UserGetGTPublicDto publicUser = UserMapper.INSTANCE.userToUserGetGTPublicDto(user);

                webSocketSessions.get(requestedUserUuid).sendMessage(
                    Action.REQUESTED_FROM_USER.getTextMessageWithData(new Gson().toJsonTree(publicUser)));
                return;
            }
            // TODO
            // data: {uuid: "<UUID>"}
            // return: REQUEST_ACCEPTED.data: <UserGetGTDto>
            case REQUEST_ACCEPT -> {
                if (!json.getAsJsonObject("data").has("uuid")) {
                    session.sendMessage(Action.ERROR_UNKNOWN_UUID.getTextMessage());
                    return;
                }

                UUID requesterUuid = UUID.fromString(json.getAsJsonObject("data").get("uuid").getAsString());

                if (requestCache.getIfPresent(requesterUuid) == null
                    || !requestCache.getIfPresent(requesterUuid).equals(userUuid)) {
                    session.sendMessage(Action.ERROR_REQUEST_NOT_FOUND.getTextMessage());
                    return;
                }

                if (!webSocketSessions.containsKey(userUuid)) {
                    session.sendMessage(Action.ERROR_REQUESTED_USER_GONE.getTextMessage());
                    return;
                }

                requestCache.invalidate(userUuid);

                // send data to requester
                Optional<User> optionalRequestedUser = userService.findUserID(userUuid);
                if (optionalRequestedUser.isEmpty()) {
                    session.sendMessage(Action.ERROR_INTERNAL_SERVER_ERROR.getTextMessage());
                    return;
                }

                UserGetGTDto requestedUserDto = UserMapper.INSTANCE.userToUserGetGTDto(optionalRequestedUser.get());

                webSocketSessions.get(requesterUuid).sendMessage(
                    Action.REQUEST_ACCEPTED.getTextMessageWithData(new Gson().toJsonTree(requestedUserDto)));

                // send data to requested
                Optional<User> optionalRequesterUser = userService.findUserID(userUuid);
                if (optionalRequesterUser.isEmpty()) {
                    session.sendMessage(Action.ERROR_INTERNAL_SERVER_ERROR.getTextMessage());
                    return;
                }

                UserGetGTDto requesterUserDto = UserMapper.INSTANCE.userToUserGetGTDto(optionalRequesterUser.get());

                webSocketSessions.get(userUuid).sendMessage(
                    Action.REQUEST_ACCEPTED.getTextMessageWithData(new Gson().toJsonTree(requesterUserDto)));

                return;
            }
            // data: {uuid: "<UUID>"}
            // return: REQUEST_DENIED.data: {uuid: "<UUID>"}
            case REQUEST_DENY -> {
                if (!json.getAsJsonObject("data").has("uuid")) {
                    session.sendMessage(Action.ERROR_UNKNOWN_UUID.getTextMessage());
                    return;
                }

                UUID requesterUuid = UUID.fromString(json.getAsJsonObject("data").get("uuid").getAsString());

                if (requestCache.getIfPresent(requesterUuid) == null
                    || !requestCache.getIfPresent(requesterUuid).equals(userUuid)) {
                    session.sendMessage(Action.ERROR_REQUEST_NOT_FOUND.getTextMessage());
                    return;
                }

                if (!webSocketSessions.containsKey(requesterUuid)) {
                    session.sendMessage(Action.ERROR_REQUESTED_USER_GONE.getTextMessage());
                    return;
                }

                requestCache.invalidate(requesterUuid);

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("uuid", userUuid.toString());
                webSocketSessions.get(requesterUuid).sendMessage(Action.REQUEST_DENIED.getTextMessageWithData(
                    jsonResponse
                ));

                return;
            }
            // no data
            default -> {
                session.sendMessage(Action.ERROR_UNKNOWN_ACTION.getTextMessage());
                return;
            }
        }
    }

    private boolean validateRequestPayload(String payload) {
        JsonElement json;

        try {
            System.out.println("PAYLOAD " + payload);
            json = JsonParser.parseString(payload);
        } catch (Exception e) {
            return false;
        }

        if (json == null) {
            return false;
        }

        if (!json.isJsonObject()) {
            return false;
        }

        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("action_id")) {
            return false;
        }

        return jsonObject.has("data");
    }

    private @Nullable JsonObject getJsonFromMessage(String json) {
        if (!validateRequestPayload(json)) {
            return null;
        }

        return JsonParser.parseString(json).getAsJsonObject();
    }

    enum Action {
        NEW_USER(1),
        REMOVE_USER(2),
        REQUEST_USER(3),
        REQUESTED_FROM_USER(4),
        REQUEST_ACCEPT(5),
        REQUEST_DENY(6),
        REQUEST_ACCEPTED(7),
        REQUEST_DENIED(8),
        JOINED_ALL_USERS(9),
        ERROR_UNKNOWN_ACTION(1001),
        ERROR_UNAUTHORIZED(1002),
        ERROR_INVALID_DATA(1003),
        ERROR_ALREADY_CONNECTED(1004),
        ERROR_UNKNOWN_UUID(1005),
        ERROR_ALREADY_REQUESTED(1006),
        ERROR_INTERNAL_SERVER_ERROR(1007),
        ERROR_REQUESTED_USER_GONE(1008),
        ERROR_REQUEST_NOT_FOUND(1009),
        ERROR_FORBIDDEN(1010);

        private final int id;

        Action(int id) {
            this.id = id;
        }

        public static Action getFrom(String value) {
            return Action.valueOf(value.toUpperCase());
        }

        public static @Nullable Action getFrom(int id) {
            return Arrays.stream(Action.values())
                .filter(a -> a.getId() == id)
                .findAny().orElseGet(() -> null);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name().toLowerCase();
        }

        public @NotNull String getJsonWithData(JsonElement data) {
            JsonObject json = new JsonObject();
            json.addProperty("action", getName());
            json.addProperty("action_id", getId());

            JsonObject uuidJson = new JsonObject();
            json.add("data", data);

            return json.toString();
        }

        public @NotNull String getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("action", getName());
            json.addProperty("action_id", getId());

            return json.toString();
        }

        public TextMessage getTextMessageWithData(JsonElement data) {
            return new TextMessage(getJsonWithData(data));
        }

        public TextMessage getTextMessage() {
            return new TextMessage(getJson());
        }
    }
}