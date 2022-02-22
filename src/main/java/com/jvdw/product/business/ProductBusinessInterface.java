package com.jvdw.product.business;

import com.jvdw.product.model.ProductModel;

import java.util.List;

/**
 * The ProductBusinessInterface defines the API for product business logic
 */
public interface ProductBusinessInterface
{
    /**
     * Stock a product into the inventory DO NOT USE to adjust quantity see the correct() method
     * @param product the product to stock
     * @return ProductBusinessResponse an enumeration value indicating the outcome of the operation
     */
    ProductBusinessResponse stock(ProductModel product);

    /**
     * Retrieve one product by its unique ID
     * @param product the product to retrieve
     * @return ProductModel | null The requested product or null if the product wasn't found
     */
    ProductModel retrieveOne(ProductModel product);

    /**
     * Retrieve all products in the inventory
     * @return List all products in the inventory
     */
    List<ProductModel> retrieveAll();

    /**
     * Correct a product's information e.g. quantity, price etc
     * @param product The product to correct with the corrections to be made
     * @return ProductBusinessResponse an enumeration value indicating the outcome of the operation
     */
    ProductBusinessResponse correct(ProductModel product);

    /**
     * Unstock a product from the inventory to adjust quantity see the correct() method
     * @param product the product to unstock
     * @return ProductBusinessResponse an enumeration value indicating the outcome of the operation
     */
    ProductBusinessResponse unstock(ProductModel product);
}
