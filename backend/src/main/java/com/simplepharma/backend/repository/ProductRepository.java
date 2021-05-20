package com.simplepharma.backend.repository;

import com.simplepharma.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductId(int productId);

    void deleteByProductId(int productId);

    List<Product> findAll();
}
