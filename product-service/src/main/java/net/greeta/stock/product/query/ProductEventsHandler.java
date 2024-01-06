package net.greeta.stock.product.query;

import net.greeta.stock.product.data.ProductEntity;
import net.greeta.stock.product.data.ProductsRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import net.greeta.stock.product.events.ProductCreatedEvent;
import net.greeta.stock.order.events.ProductReservedEvent;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

	private final ProductsRepository productsRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);

	public ProductEventsHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	@ExceptionHandler(resultType=Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}
	
	@ExceptionHandler(resultType=IllegalArgumentException.class)
	public void handle(IllegalArgumentException exception) {
		// Log error message
	}
	

	@EventHandler
	public void on(ProductCreatedEvent event) {

		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(event, productEntity);

		try {
			productsRepository.save(productEntity);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}

	}
	
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());
		
		LOGGER.debug("ProductReservedEvent: Current product quantity " + productEntity.getQuantity());
		
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		
		
		productsRepository.save(productEntity);
		
		LOGGER.debug("ProductReservedEvent: New product quantity " + productEntity.getQuantity());
 	
		LOGGER.info("ProductReservedEvent is called for productId:" + productReservedEvent.getProductId() +
				" and orderId: " + productReservedEvent.getOrderId());
	}
	
	@ResetHandler
	public void reset() {
		productsRepository.deleteAll();
	}

}
