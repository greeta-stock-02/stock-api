package net.greeta.stock.gateway.client;

import net.greeta.erp.services.department.model.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "department")
public interface DepartmentTestClient {

    @GetMapping("/{id}")
    public Department findById(@PathVariable("id") String id);

    @PostMapping("/")
    public Department add(@RequestBody Department department);

    @GetMapping("/")
    public Iterable<Department> findAll();

    @GetMapping("/organization/{organizationId}")
    public List<Department> findByOrganization(@PathVariable("organizationId") String organizationId);
}
