package com.simple.pharma.source.repository;

import java.util.List;

import com.simple.pharma.source.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByProductid(int productid);

	void deleteByProductid(int productid);

	List<Product> findAll();
}
