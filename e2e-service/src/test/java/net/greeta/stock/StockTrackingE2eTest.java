package net.greeta.stock;

import net.greeta.stock.order.OrderClient;
import net.greeta.stock.product.ProductClient;
import net.greeta.stock.order.OrderTestDataService;
import net.greeta.stock.product.ProductTestDataService;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = {
        "classpath:application.yml",
        "classpath:application-test.yml"
})
public class StockTrackingE2eTest {

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderTestDataService orderTestDataService;

    @Autowired
    private ProductTestDataService productTestDataService;

    @BeforeEach
    void cleanup() {
        orderTestDataService.resetDatabase();
        productTestDataService.resetDatabase();
    }

    @Test
    void createProductTest() {
        CreateProductDto product = new CreateProductDto("test", BigDecimal.valueOf(10.00), 5);
        String productId = productClient.create(product);
        assertNotNull(productId);
        ProductDto result = productClient.get(productId);
        assertEquals(productId, result.getProductId());
        assertEquals("test", result.getTitle());
        assertEquals(10.00, result.getPrice());
        assertEquals(5, result.getQuantity());
    }


}
