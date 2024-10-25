package com.shopme.admin.brands;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

	
	@Autowired
	private BrandRepository repo;
	
	@Test 
	public void testCreateBrand1(){
		
		Category laptops = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);
		
		Brand savedBrand = repo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repo.findById(1).get();
		samsung.setName(newName);
		
		Brand savedBrand = repo.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo("Samsung Electronics");
	}
	
	
	@Test 
	public void testDelete() {
		Integer id = 2;
		repo.deleteById(id);
		
		Optional<Brand> result = repo.findById(id);
		
		assertThat(result.isEmpty());
	}
}
