package com.simplepharma.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = -7446162716367847201L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String description;
    private String productName;
    private double price;
    private int quantity;
    @Lob
    private byte[] productImage;

}
