package com.example.task.employee.controller;

import com.example.task.employee.exception.EmployeeNotFoundException;
import com.example.task.employee.model.Employee;
import com.example.task.employee.repository.EmployeeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Add new employee.
     *
     * Accepts all parameters except Id. Id will be automatically generated for all the requests.
     * @param employee {@link Employee}
     * @return @{@link Employee}
     */
    @PostMapping(value = "/")
    public Employee addEmployee(@RequestBody  Employee employee) {
        if(StringUtils.isAnyEmpty(employee.getName(), employee.getDepartment())){
            throw new IllegalArgumentException("input values are invalid");
        }
        employee.setId(null); //Ignore id if passed by client
        return employeeRepository.save(employee);
    }

    /**
     * Return employee for the given Id.
     *
     * @param id Id of the employee.
     * @throws EmployeeNotFoundException if no employee found.
     * @return @{@link Employee}
     */
    @GetMapping(value = "/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }

    /**
     * Returns all the employees
     *
     * @return List of {@link Employee}
     */
    @GetMapping(value = "/")
    @ResponseBody
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        employeeRepository.findAll().forEach(employees::add);
        return employees;
    }

    /**
     * Delete employee for the given Id.
     *
     * @param id Id of the employee
     * @throws EmployeeNotFoundException if employee not found for the given id.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        employeeRepository.delete(employee);
    }
}
