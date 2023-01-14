package com.jvdw.product.data.entity;

import com.jvdw.product.model.ProductModel;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * The Product Entity represents a product in the database
 */
@Document("Product")
// lombok annotations used to reduce boilerplate code
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductEntity
{
    // the unique database ID
    @Id
    private String id;

    // product name
    private String name;

    // product description
    private String description;

    // product prices
    private BigDecimal price;

    // product quantity
    private int quantity;

    public ProductEntity(String id)
    {
        this.id = id;
    }

    /**
     * This constructor is used to initialize all fields except the ID
     * @param name the product's name
     * @param description the product's description
     * @param price the product's price
     * @param quantity the product's quantity
     */
    public ProductEntity(String name, String description, BigDecimal price, int quantity)
    {
        // set field values from parameters
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
}
