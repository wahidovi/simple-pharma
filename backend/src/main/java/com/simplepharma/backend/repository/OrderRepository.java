package com.simplepharma.backend.repository;

import com.simplepharma.backend.model.PlaceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<PlaceOrder, Long> {

    PlaceOrder findByOrderId(int orderId);

    List<PlaceOrder> findAll();
}
