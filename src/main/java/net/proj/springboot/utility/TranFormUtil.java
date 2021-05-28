package net.proj.springboot.utility;

import net.proj.springboot.loginEmployee.EmployeeEntry;
import net.proj.springboot.model.Employee;
import net.proj.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranFormUtil {

    public Employee savingEmployee(EmployeeEntry ep){
        Employee newEmployee = new Employee();
        newEmployee.setEmail(ep.getEmailId());
        newEmployee.setPassword(ep.getPassword());
        newEmployee.setName(ep.getUserName());
        return newEmployee;
    }

}
