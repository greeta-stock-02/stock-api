package net.greeta.stock.foodordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class FoodOrderingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodOrderingServiceApplication.class, args);
    }
}
