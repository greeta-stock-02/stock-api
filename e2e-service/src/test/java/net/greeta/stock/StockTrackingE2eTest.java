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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
    private AxonClient axonClient;

    @Autowired
    private OAuth2Client oAuth2Client;

    @Autowired
    private OrderTestDataService orderTestDataService;

    @Autowired
    private ProductTestDataService productTestDataService;

    @BeforeEach
    void cleanup() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "admin");
        params.put("password", "admin");
        params.put("grant_type", "password");
        params.put("client_id", "stock-app");
        var result = axonClient.purgeEvents();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        var token = oAuth2Client.getToken(params);
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
