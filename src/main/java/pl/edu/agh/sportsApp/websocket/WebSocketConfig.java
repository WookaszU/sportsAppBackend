package pl.edu.agh.sportsApp.websocket;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.useractivity.UserActivity;
import pl.edu.agh.sportsApp.websocket.principal.SocketPrincipal;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @NonNull
    SocketActionsInterceptor socketActionsInterceptor;
    @NonNull
    UserActivity userActivity;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/sports-app").setHandshakeHandler(new MyHandshakeHandler());//.withSockJS();
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(socketActionsInterceptor);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        // sending messages from server timeout set to 3 seconds
        registration.setSendTimeLimit(120000);
    }

    public class MyHandshakeHandler extends DefaultHandshakeHandler {
        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                          Map<String, Object> attributes) {
            List<String> authHeader = request.getHeaders().get("authorization");
            if(authHeader == null || authHeader.size() != 1)
                return null;

            Optional<User> userOpt = userActivity.findUserByToken(authHeader.get(0));
            if(!userOpt.isPresent())
                return null;

            User user = userOpt.get();

            return SocketPrincipal.builder()
                    .id(user.getId())
                    .idAsString(Long.toString(user.getId()))
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .photoId(user.getUserPhoto() != null ? user.getUserPhoto().getPhotoId() : "0")
                    .build();
        }
    }

}
