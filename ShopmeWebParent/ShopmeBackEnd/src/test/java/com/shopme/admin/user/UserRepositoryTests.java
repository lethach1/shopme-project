package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userCheck = new User("levanthach.ibba@gmail.com", "jelly100700", "jelly", "le");
		userCheck.addRole(roleAdmin);

		User savedUser = repo.save(userCheck);

		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRavi = new User("Emailtesting.com", "12345", "Test", "Kuma");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);

		User savedUser = repo.save(userRavi);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));	
	}
	
	@Test
	public void testGetUserById() {
		User userCheck = repo.findById(1).get();
		assertThat(userCheck).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetals() {
		User userCheck = repo.findById(1).get();
		userCheck.setEnabled(true);
		userCheck.setEmail("Akabangap@gmail.com");
		
		repo.save(userCheck);
	}
	
	@Test
	public void testUpdateUserRole() {
		User userRavi = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userRavi.getRoles().remove(userRavi);
		userRavi.addRole(roleSalesperson);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "levanthach.ibba@gmail.com";
		User user = repo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisabledUser() {
		Integer id = 10;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnabledUser() {
		Integer id = 10;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber,pageSize);
		Page<User> page = repo.findAll(pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	
	@Test
	public void testSearchUsers() {
		String keyword = "thach";
		
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber,pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isGreaterThan(0); 
	}
}
