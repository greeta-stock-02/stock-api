package net.greeta.stock.user;

import com.thoughtworks.xstream.XStream;
import net.greeta.stock.user.interceptor.CommandLoggingDispatchInterceptor;
import net.greeta.stock.user.interceptor.EventLoggingDispatchInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class UsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
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

	@Autowired
	public void registerInterceptors(CommandBus commandBus, EventBus eventBus) {
		commandBus.registerDispatchInterceptor(new CommandLoggingDispatchInterceptor());
		eventBus.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
	}

}
