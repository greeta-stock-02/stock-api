package net.greeta.stock.customer.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages
        .simpTypeMatchers(
            SimpMessageType.CONNECT,
            SimpMessageType.SUBSCRIBE
        )
        .hasAnyRole(STOCK_MANAGER, STOCK_USER)
        .simpTypeMatchers(SimpMessageType.DISCONNECT)
        .authenticated()
        .anyMessage().denyAll();
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }

  public static final String STOCK_MANAGER = "STOCK_MANAGER";
  public static final String STOCK_USER = "STOCK_USER";

}
