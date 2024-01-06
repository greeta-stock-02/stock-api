package net.greeta.stock.gateway;

import net.greeta.erp.services.department.model.Department;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.kubernetes.discovery.enabled=false",
                "spring.cloud.kubernetes.config.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class DepartmentAPITest {

    @Container
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:4.4");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongodb::getReplicaSetUrl);
    }

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void addDepartmentTest() {
        Department department = new Department("1", "Test");
        department = restTemplate.postForObject("/", department, Department.class);
        assertNotNull(department);
        assertNotNull(department.getId());
    }

    @Test
    void addAndThenFindDepartmentByIdTest() {
        Department department = new Department("2", "Test2");
        department = restTemplate.postForObject("/", department, Department.class);
        assertNotNull(department);
        assertNotNull(department.getId());
        department = restTemplate.getForObject("/{id}", Department.class, department.getId());
        assertNotNull(department);
        assertNotNull(department.getId());
    }

    @Test
    void findAllDepartmentsTest() {
        Department[] departments = restTemplate.getForObject("/", Department[].class);
        assertEquals(2, departments.length);
    }

    @Test
    void findDepartmentsByOrganizationTest() {
        Department[] departments = restTemplate.getForObject("/organization/1", Department[].class);
        assertEquals(1, departments.length);
    }

}
