package net.greeta.stock.customer.config;

import net.greeta.stock.customer.interceptor.CommandLoggingDispatchInterceptor;
import net.greeta.stock.customer.interceptor.EventLoggingDispatchInterceptor;
import com.thoughtworks.xstream.XStream;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Autowired
    public void registerDispatchInterceptor(CommandBus commandBus, EventBus eventBus) {
        commandBus.registerDispatchInterceptor(new CommandLoggingDispatchInterceptor());
        eventBus.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
    }

    // Workaround to avoid the exception "com.thoughtworks.xstream.security.ForbiddenClassException"
    // https://stackoverflow.com/questions/70624317/getting-forbiddenclassexception-in-axon-springboot
    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{
                "net.greeta.stock.**",
                "org.hibernate.proxy.pojo.bytebuddy.**"
        });
        return xStream;
    }
}
