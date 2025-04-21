package com.dot1.ticket_track;
import com.dot1.ticket_track.entity.mEmployeeMaster;
import com.dot1.ticket_track.entity.mRoleMaster;
import com.dot1.ticket_track.entity.mUserLogin_demo;
import com.dot1.ticket_track.repository.EmployeeRepository;
import com.dot1.ticket_track.repository.RoleRepository;
import com.dot1.ticket_track.repository.UserLogin_demoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories("com.dot1.ticket_track.repository")
public class TicketTrackApplication
		implements CommandLineRunner {


	@Autowired
 	private UserLogin_demoRepo userRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private  PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;


	public static void main(String[] args) {
		SpringApplication.run(TicketTrackApplication.class, args);
	}

	@Transactional
    public void run(String... args){

        List<mUserLogin_demo> adminAccount = userRepository.findByrole("ADMIN");

        if(adminAccount.isEmpty()) {

			mEmployeeMaster employeeMa = new mEmployeeMaster();

			mRoleMaster mRoleMaster1 = roleRepository.findByroleName("ADMIN").orElse(null);
			if(mRoleMaster1==null){
			mRoleMaster1.setRoleName("ADMIN");
			mRoleMaster1.setRoleDescription("this is a administration.");
			mRoleMaster1.setIsActive(true);
				mRoleMaster save = roleRepository.save(mRoleMaster1);

				employeeMa.setRoleMaster_id(save);

			}else{
				mRoleMaster mRoleMaster = roleRepository.findByroleName("ADMIN").orElseThrow(() ->new RuntimeException("Role not found"));
				employeeMa.setRoleMaster_id(mRoleMaster);
			}
			employeeMa.setEmpId(employeeRepository.newempID());
			employeeMa.setEmpCode("Emp0001");
			employeeMa.setEmpName("Mayur Patil");
			employeeMa.setIsActive(true);
			employeeMa.setModulesMaster_id(null);
			employeeMa.setPhoneNo("7219012920");
			String dateString = "2024-11-19 00:00:00.000000";
			// Define a formatter that matches the input format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
			// Parse the string into LocalDateTime
			LocalDateTime dateOFJoin = LocalDateTime.parse(dateString, formatter);
			employeeMa.setDateOfJoining(dateOFJoin);
			employeeMa.setEmailId("mayur.patil@dot1.in");
			mEmployeeMaster saveEmp = employeeRepository.save(employeeMa);
			System.out.println("Saved Employee ID: " + saveEmp.getEmpId());
			mUserLogin_demo user = new mUserLogin_demo();
			user.setLogId(userRepository.newIdUserLogin());
			user.setUserID(saveEmp.getEmailId());
			mEmployeeMaster emp = new mEmployeeMaster();
			emp.setEmpId(saveEmp.getEmpId());
			user.setEmp_Id(emp);
			Integer empId = emp.getEmpId();
			mEmployeeMaster employeeMaster = employeeRepository.findById(empId).orElse(null);
			if (employeeMaster != null) {

				String roleName = employeeMaster.getRoleMaster_id().getRoleName();
				user.setRole(roleName);
			}
			user.setIsactUser(false);
			user.setUserPWD(passwordEncoder.encode("admin@12345"));
			mUserLogin_demo saveLogin = userRepository.save(user);
			System.out.println("Saved USerLogin ID: " + saveLogin.getUserID());


		}
		}






}