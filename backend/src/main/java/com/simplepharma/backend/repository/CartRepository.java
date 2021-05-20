package com.simplepharma.backend.repository;

import com.simplepharma.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByEmail(String email);

    Cart findByCartIdAndEmail(int bufcartId, String email);

    void deleteByCartIdAndEmail(int bufcartId, String email);

    List<Cart> findAllByEmail(String email);

    List<Cart> findAllByOrderId(int orderId);
}
