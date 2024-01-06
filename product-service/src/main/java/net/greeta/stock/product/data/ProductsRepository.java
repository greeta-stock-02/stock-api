package net.greeta.stock.product.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<ProductEntity, String> {
	
	ProductEntity findByProductId(String productId);
	ProductEntity findByProductIdOrTitle(String productId, String title);

}
