package net.proj.springboot.service;
import net.proj.springboot.exception.ResourceNotFoundException;
import net.proj.springboot.loginEmployee.EmployeeEntry;
import net.proj.springboot.loginEmployee.GetEmployee;
import net.proj.springboot.loginEmployee.StatusResponse;
import net.proj.springboot.loginEmployee.ReturnEmployeeWithToken;
import net.proj.springboot.model.Employee;
import net.proj.springboot.repository.EmployeeRepository;
import net.proj.springboot.utility.TranFormUtil;
import net.proj.springboot.utility.UtilFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired public EmployeeRepository employeeRepo;
    @Autowired UtilFunctions utilFunctions;
    @Autowired TranFormUtil tranFormUtil;

    public ResponseEntity<StatusResponse> signup(@RequestBody EmployeeEntry employeeEntry){
        StatusResponse res = new StatusResponse();
        Employee employee = employeeRepo.findByEmailId(employeeEntry.getEmailId());
        if(employee==null){
            String hashPassWord = utilFunctions.hashedPassword(employeeEntry.getPassword());
            employeeEntry.setPassword(hashPassWord);
            Employee employeeToBeStored = tranFormUtil.savingEmployee(employeeEntry);
            employeeRepo.save(employeeToBeStored);
            res.setStatus("Successfully Signed In");
            res.setStatusCode(1200);
            return new ResponseEntity<StatusResponse>(res,HttpStatus.OK);
        }
        else{
            res.setStatus("User Already Exists");
            res.setStatusCode(1208);
            return new ResponseEntity<StatusResponse>(res, HttpStatus.ALREADY_REPORTED);
        }
    }

    public ResponseEntity<?> login(@RequestBody EmployeeEntry employeeEntry){
        StatusResponse res = new StatusResponse();
        Employee employee = employeeRepo.findByEmailId(employeeEntry.getEmailId());
        if(employee==null){
            res.setStatus("Employee doesn't exists");
            res.setStatusCode(1404);
            return new ResponseEntity<StatusResponse>(res,HttpStatus.NOT_FOUND);
        }
        else{
            String hashPassword = employee.getPassword();
            String inputHashPassword = utilFunctions.hashedPassword(employeeEntry.getPassword());
            if(hashPassword.equals(inputHashPassword)){
                ReturnEmployeeWithToken resToken = new ReturnEmployeeWithToken();
                resToken.setStatus("Successfully logged in");
                resToken.setStatusCode(1200);
                String token = utilFunctions.generateToken(employeeEntry);
                resToken.setToken(token);
                return new ResponseEntity<ReturnEmployeeWithToken>(resToken, HttpStatus.valueOf(200));
            }
            else{
                res.setStatus("Password Incorrect");
                res.setStatusCode(1400);
                return new ResponseEntity<StatusResponse>(res,HttpStatus.BAD_REQUEST);
            }
        }
    }

    public ResponseEntity<GetEmployee> getEmployeeById(@PathVariable Long id){
        Employee employee;
        employee = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not exists with id : " + id));
        GetEmployee res = new GetEmployee();
        res.setStatusCode(1800);
        res.setStatus("Got the Employee");
        res.setEmailId(employee.getEmail());
        res.setUserName(employee.getName());
        return new ResponseEntity<GetEmployee>(res,HttpStatus.OK);
    }

    public ResponseEntity<StatusResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeEntry employeeEntry){
        // findById(id) returns an optional employee hence we need a orElseThrow to see if the employee doesn't exist in the database.
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not exists with id : "+id));
        if(employeeEntry.getEmailId()!=null) employee.setEmail(employeeEntry.getEmailId());
        if(employeeEntry.getPassword()!=null) employee.setPassword(employeeEntry.getPassword());
        if(employeeEntry.getUserName()!=null) employee.setName(employeeEntry.getUserName());
        employeeRepo.save(employee);
        StatusResponse res = new StatusResponse();
        res.setStatusCode(1900);
        res.setStatus("Employee Successfully Updated");
        return new ResponseEntity<StatusResponse>(res,HttpStatus.ACCEPTED);
    }

    public ResponseEntity<HashMap<String, Long>> deleteEmployee(@PathVariable Long id){
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not exists with id : "+id));
        employeeRepo.delete(employee);
        HashMap<String, Long> res = new HashMap<>();
        res.put("Employee deleted with id: ",employee.getId());
        return ResponseEntity.ok(res);
    }

    public List<Employee> getEmployees(){
        return employeeRepo.findAll();
    }

    public void clearDatabase(){
        List<Employee> employees = employeeRepo.findAll();
        for(Employee e:employees) employeeRepo.delete(e);
    }
}


