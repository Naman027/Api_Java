package net.proj.springboot.controller;
import net.proj.springboot.loginEmployee.EmployeeEntry;
import net.proj.springboot.loginEmployee.EmployeeResponse;
import net.proj.springboot.loginEmployee.StatusResponse;
import net.proj.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.proj.springboot.model.Employee;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.POST,value = "/signup")
    public ResponseEntity<StatusResponse> signup(@RequestBody EmployeeEntry e){
        return employeeService.signup(e);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/login")
    public ResponseEntity<?> login(@RequestBody EmployeeEntry e){
        return employeeService.login(e);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/refreshToken/{token}")
    public ResponseEntity<?> refreshToken(@PathVariable String token){
        return employeeService.refreshToken(token);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable  Long id){
        return employeeService.getEmployeeById(id);
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/{id}")
    public ResponseEntity<StatusResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeEntry employeeEntry){
        return employeeService.updateEmployee(id, employeeEntry);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<HashMap<String, Long>> deleteEmployee(@PathVariable Long id){
        return employeeService.deleteEmployee(id);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<Employee> getEmployees(){
        return employeeService.getEmployees();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/deleteall")
    public void clearDatabase(){
        employeeService.clearDatabase();
    }
}


