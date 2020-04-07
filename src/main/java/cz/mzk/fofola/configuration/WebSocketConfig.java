package cz.mzk.fofola.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/tree", "/check", "/processes");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/remove-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/transfer-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/tree-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/reindex-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/check-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/rights-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/process-info").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/process-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
        registry.addEndpoint("/process-manipulation-websocket").addInterceptors(new IpHandshakeInterceptor()).withSockJS();
    }

}