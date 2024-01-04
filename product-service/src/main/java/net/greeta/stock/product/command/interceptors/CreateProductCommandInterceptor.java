package net.greeta.stock.product.command.interceptors;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.greeta.stock.product.command.CreateProductCommand;
import net.greeta.stock.product.core.data.ProductLookupEntity;
import net.greeta.stock.product.core.data.ProductLookupRepository;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
	private final ProductLookupRepository productLookupRepository;
	
	public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}
 
	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages) {
		 
		return (index, command) -> {
			
			LOGGER.info("Intercepted command: " + command.getPayloadType());
			
			if(CreateProductCommand.class.equals(command.getPayloadType())) {
				
				CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();
				
				ProductLookupEntity productLookupEntity =  productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(),
						createProductCommand.getTitle());
				
				if(productLookupEntity != null) {
					throw new IllegalStateException(
							String.format("Product with productId %s or title %s already exist", 
									createProductCommand.getProductId(), createProductCommand.getTitle())
							);
				}

			}
			
			return command;
		};
	}

}
