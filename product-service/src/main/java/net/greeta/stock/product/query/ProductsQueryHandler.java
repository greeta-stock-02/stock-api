package net.greeta.stock.product.query;

import java.util.ArrayList;
import java.util.List;

import net.greeta.stock.product.data.ProductEntity;
import net.greeta.stock.product.data.ProductsRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import net.greeta.stock.product.dto.ProductDto;

@Component
public class ProductsQueryHandler {
	
	private final ProductsRepository productsRepository;
	
	public ProductsQueryHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	@QueryHandler
	public List<ProductDto> findProducts(FindProductsQuery query) {
		
		List<ProductDto> productsRest = new ArrayList<>();
		
		List<ProductEntity> storedProducts =  productsRepository.findAll();
		
		for(ProductEntity productEntity: storedProducts) {
			ProductDto productRestModel = new ProductDto();
			BeanUtils.copyProperties(productEntity, productRestModel);
			productsRest.add(productRestModel);
		}
		
		return productsRest;
		
	}

	@QueryHandler
	public ProductDto findProduct(FindProductQuery query) {
		ProductEntity productEntity =  productsRepository.findByProductId(query.getProductId());
		ProductDto productRestModel = new ProductDto();
		BeanUtils.copyProperties(productEntity, productRestModel);
		return productRestModel;
	}

}
