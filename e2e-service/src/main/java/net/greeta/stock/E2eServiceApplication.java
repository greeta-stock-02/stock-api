package net.greeta.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class E2eServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(E2eServiceApplication.class, args);
    }
}
