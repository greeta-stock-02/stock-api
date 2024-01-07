package net.greeta.stock;

import net.greeta.stock.client.AxonClient;
import net.greeta.stock.order.OrderClient;
import net.greeta.stock.order.OrderTestDataService;
import net.greeta.stock.product.ProductClient;
import net.greeta.stock.product.ProductTestDataService;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = {
        "classpath:application.yml"
})
public class StockTrackingE2eTest {

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private AxonClient axonClient;

    @Autowired
    private OrderTestDataService orderTestDataService;

    @Autowired
    private ProductTestDataService productTestDataService;

    @BeforeEach
    void cleanup() {
        var result = axonClient.purgeEvents();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        orderTestDataService.resetDatabase();
        productTestDataService.resetDatabase();
    }

    @Test
    void createProductTest() {
        CreateProductDto product = new CreateProductDto("test2", BigDecimal.valueOf(10.00), 5);
        String productId = productClient.create(product);
        assertNotNull(productId);
        ProductDto result = productClient.get(productId);
        assertEquals(productId, result.getProductId());
        assertEquals("test", result.getTitle());
        assertEquals(BigDecimal.valueOf(10.00), result.getPrice());
        assertEquals(5, result.getQuantity());
    }


}
