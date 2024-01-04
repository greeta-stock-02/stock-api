package net.greeta.stock.product;

import com.thoughtworks.xstream.XStream;
import net.greeta.stock.product.command.interceptors.CreateProductCommandInterceptor;
import net.greeta.stock.product.interceptor.CommandLoggingDispatchInterceptor;
import net.greeta.stock.product.interceptor.EventLoggingDispatchInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import net.greeta.stock.product.core.errorhandling.ProductsServiceEventsErrorHandler;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
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
	public void registerInterceptors(ApplicationContext context,
														CommandBus commandBus, EventBus eventBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
		commandBus.registerDispatchInterceptor(new CommandLoggingDispatchInterceptor());
		eventBus.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
	}

	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group",
				conf -> new ProductsServiceEventsErrorHandler());

//		config.registerListenerInvocationErrorHandler("product-group",
//				conf -> PropagatingErrorHandler.instance());
	}

	@Bean(name="productSnapshotTriggerDefinition")
	public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
		return new EventCountSnapshotTriggerDefinition(snapshotter, 3);
	}
}
