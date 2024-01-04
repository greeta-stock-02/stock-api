package net.greeta.stock.order;

import com.thoughtworks.xstream.XStream;
import net.greeta.stock.order.interceptor.CommandLoggingDispatchInterceptor;
import net.greeta.stock.order.interceptor.EventLoggingDispatchInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class OrdersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersServiceApplication.class, args);
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
	public void registerDispatchInterceptor(CommandBus commandBus, EventBus eventBus) {
		commandBus.registerDispatchInterceptor(new CommandLoggingDispatchInterceptor());
		eventBus.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
	}

	/*@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("order-group",
				conf -> PropagatingErrorHandler.instance());
	}*/

	@Bean
	public DeadlineManager deadlineManager(org.axonframework.config.Configuration configuration,
										   SpringTransactionManager transactionManager) {

		return SimpleDeadlineManager.builder()
				.scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
				.transactionManager(transactionManager)
				.build();
	}

}
