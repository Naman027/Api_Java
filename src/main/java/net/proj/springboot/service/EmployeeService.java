package net.proj.springboot.service;
import com.fasterxml.uuid.Generators;
import net.proj.springboot.exception.ResourceNotFoundException;
import net.proj.springboot.model.EmployeeEntry;
import net.proj.springboot.model.EmployeeResponse;
import net.proj.springboot.model.StatusResponse;
import net.proj.springboot.model.ReturnEmployeeWithToken;
import net.proj.springboot.model.Employee;
import net.proj.springboot.model.EmployeeSessions;
import net.proj.springboot.repository.EmployeeRepository;
import net.proj.springboot.repository.EmployeeSessionRepository;
import net.proj.springboot.utility.TranFormUtil;
import net.proj.springboot.utility.UtilFunctions;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;

@Service
public class EmployeeService {
    final EmployeeRepository employeeRepo;
    final UtilFunctions utilFunctions;
    final TranFormUtil tranFormUtil;
    final EmployeeSessionRepository employeeSessionRepository;

    public EmployeeService(EmployeeRepository employeeRepo, UtilFunctions utilFunctions, TranFormUtil tranFormUtil, EmployeeSessionRepository employeeSessionRepository) {
        this.employeeRepo = employeeRepo;
        this.utilFunctions = utilFunctions;
        this.tranFormUtil = tranFormUtil;
        this.employeeSessionRepository = employeeSessionRepository;
    }

    public ResponseEntity<StatusResponse> signup(@RequestBody EmployeeEntry employeeEntry){
        StatusResponse res = new StatusResponse();
        Employee employee = employeeRepo.findByEmailId(employeeEntry.getEmailId());
        if(employee==null){
            String hashPassWord = utilFunctions.hashedPassword(employeeEntry.getPassword());
            employeeEntry.setPassword(hashPassWord);
            Employee employeeToBeStored = tranFormUtil.savingEmployee(employeeEntry);
            employeeRepo.save(employeeToBeStored);
            res.setStatus("Successfully Signed Up");
            res.setStatusCode(1200);
            return new ResponseEntity<>(res,HttpStatus.OK);
        }
        else{
            res.setStatus("User Already Exists");
            res.setStatusCode(1208);
            return new ResponseEntity<>(res, HttpStatus.ALREADY_REPORTED);
        }
    }

    public ResponseEntity<?> login(@RequestBody EmployeeEntry employeeEntry){
        StatusResponse res = new StatusResponse();
        Employee employee = employeeRepo.findByEmailId(employeeEntry.getEmailId());
        if(employee==null){
            res.setStatus("Employee doesn't exists");
            res.setStatusCode(1404);
            return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
        }
        else{
            String hashPassword = employee.getPassword();
            String inputHashPassword = utilFunctions.hashedPassword(employeeEntry.getPassword());
            if(hashPassword.equals(inputHashPassword)){
                ReturnEmployeeWithToken resToken = new ReturnEmployeeWithToken();
                resToken.setStatus("Successfully logged in");
                resToken.setStatusCode(1200);
                String token = utilFunctions.generateToken(employeeEntry.getEmailId());
                resToken.setToken(token);
                UUID uuid = Generators.timeBasedGenerator().generate();
                EmployeeSessions employeeSessions = new EmployeeSessions();
                employeeSessions.setEmailId(employee.getEmailId());
                employeeSessions.setDateId(uuid.toString());
                employeeSessionRepository.save(employeeSessions);
                return new ResponseEntity<>(resToken, HttpStatus.valueOf(200));
            }
            else{
                res.setStatus("Password Incorrect");
                res.setStatusCode(1400);
                return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
            }
        }
    }

    public ResponseEntity<?> paymentMethod(EmployeeEntry employeeEntry){
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus("Now you can continue with Payment");
        statusResponse.setStatusCode(1000);
        return new ResponseEntity<>(statusResponse, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id){
        Employee employee;
        employee = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not exists with id : " + id));
        EmployeeResponse res = new EmployeeResponse();
        res.setStatusCode(1800);
        res.setStatus("Got the Employee");
        res.setEmailId(employee.getEmailId());
        res.setUserName(employee.getUserName());
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    public ResponseEntity<StatusResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeEntry employeeEntry){
        // findById(id) returns an optional employee hence we need a orElseThrow to see if the employee doesn't exist in the database.
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not exists with id : "+id));
        if(employeeEntry.getEmailId()!=null) employee.setEmailId(employeeEntry.getEmailId());
        if(employeeEntry.getPassword()!=null) employee.setPassword(employeeEntry.getPassword());
        if(employeeEntry.getUserName()!=null) employee.setUsername(employeeEntry.getUserName());
        employeeRepo.save(employee);
        StatusResponse res = new StatusResponse();
        res.setStatusCode(1900);
        res.setStatus("Employee Successfully Updated");
        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
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


