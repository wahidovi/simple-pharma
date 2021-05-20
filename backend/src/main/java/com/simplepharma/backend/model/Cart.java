package com.simplepharma.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Cart implements Serializable {

    private static final long serialVersionUID = 4049687597028261161L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;

    @Column(name = "order_id", nullable = true)
    private int orderId;

    private String email;

    @Column(name = "date_added")
    private Date dateAdded;

    private int quantity;
    private double price;
    @Column(name = "product_id")
    private int productId;

    private String productname;
}
