package com.simple.pharma.source.repository;

import java.util.List;

import com.simple.pharma.source.model.Bufcart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Bufcart, Long> {

	List<Bufcart> findByEmail(String email);

	Bufcart findByBufcartIdAndEmail(int bufcartId, String email);

	void deleteByBufcartIdAndEmail(int bufcartId, String email);

	List<Bufcart> findAllByEmail(String email);

	List<Bufcart> findAllByOrderId(int orderId);
}
