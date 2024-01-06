package net.greeta.stock.gateway;

import net.greeta.erp.services.department.client.DepartmentTestClient;
import net.greeta.erp.services.department.client.TestDataClient;
import net.greeta.erp.services.department.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = {
        "classpath:application.yml",
        "classpath:application-test.yml"
})
public class DepartmentE2eTest {

    @Autowired
    private DepartmentTestClient departmentClient;

    @Autowired
    private TestDataClient testDataClient;

    @BeforeEach
    void cleanup() {
        testDataClient.deleteAllDepartments();
    }

    @Test
    void addDepartmentTest() {
        Department department = new Department("1", "Test");
        department = departmentClient.add(department);
        assertNotNull(department);
        assertNotNull(department.getId());
    }

    @Test
    void addAndThenFindDepartmentByIdTest() {
        Department department = new Department("2", "Test2");
        department = departmentClient.add(department);
        assertNotNull(department);
        assertNotNull(department.getId());
        department = departmentClient.findById(department.getId());
        assertNotNull(department);
        assertNotNull(department.getId());
    }

    @Test
    void findAllDepartmentsTest() {
        Department[] departments = StreamSupport.stream(departmentClient.findAll().spliterator(), false).toArray(Department[]::new);
        assertEquals(0, departments.length);
    }

    @Test
    void findDepartmentsByOrganizationTest() {
        Department department = new Department("1", "Test");
        department = departmentClient.add(department);
        assertNotNull(department);
        assertNotNull(department.getId());
        List<Department> departments = departmentClient.findByOrganization("1");
        assertEquals(1, departments.size());
    }

}
