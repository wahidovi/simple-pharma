package com.simple.pharma.source.repository;

import java.util.List;

import com.simple.pharma.source.model.PlaceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<PlaceOrder, Long> {

	PlaceOrder findByOrderId(int orderId);

	List<PlaceOrder> findAll();

}
