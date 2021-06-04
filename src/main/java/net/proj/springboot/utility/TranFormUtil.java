package net.proj.springboot.utility;

import net.proj.springboot.model.EmployeeEntry;
import net.proj.springboot.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class TranFormUtil {

    public Employee savingEmployee(EmployeeEntry ep){
        Employee newEmployee = new Employee();
        newEmployee.setEmailId(ep.getEmailId());
        newEmployee.setPassword(ep.getPassword());
        newEmployee.setUsername(ep.getUserName());
        return newEmployee;
    }

}
