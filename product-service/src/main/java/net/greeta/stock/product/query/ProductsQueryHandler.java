package net.greeta.stock.product.query;

import java.util.ArrayList;
import java.util.List;

import net.greeta.stock.product.core.data.ProductEntity;
import net.greeta.stock.product.core.data.ProductsRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import net.greeta.stock.product.query.rest.ProductRestModel;

@Component
public class ProductsQueryHandler {
	
	private final ProductsRepository productsRepository;
	
	public ProductsQueryHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	@QueryHandler
	public List<ProductRestModel> findProducts(FindProductsQuery query) {
		
		List<ProductRestModel> productsRest = new ArrayList<>();
		
		List<ProductEntity> storedProducts =  productsRepository.findAll();
		
		for(ProductEntity productEntity: storedProducts) {
			ProductRestModel productRestModel = new ProductRestModel();
			BeanUtils.copyProperties(productEntity, productRestModel);
			productsRest.add(productRestModel);
		}
		
		return productsRest;
		
	}

}
