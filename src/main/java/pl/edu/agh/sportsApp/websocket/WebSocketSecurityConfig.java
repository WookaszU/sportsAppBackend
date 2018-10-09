package pl.edu.agh.sportsApp.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    // TODO check here, change to authenticated
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpSubscribeDestMatchers("/topic/**").permitAll()
                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
                .simpDestMatchers("/sportsapp/**").permitAll()
                .simpDestMatchers("/queue/**","/topic/**").denyAll();
    }

    // TODO change to false, then it is required to send csrf token in header
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}
