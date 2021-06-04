package net.proj.springboot.repository;

import net.proj.springboot.model.EmployeeSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSessionRepository extends JpaRepository<EmployeeSessions, Long> {

}
