package com.jvdw.product.business;

import com.jvdw.product.data.entity.ProductEntity;
import com.jvdw.product.data.repository.ProductRepository;
import com.jvdw.product.model.ProductModel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The ProductBusinessService performs business logic and enforces business rules for products
 */
@Service
// @Setter is used here to support unit testing
@Setter
public class ProductBusinessService implements ProductBusinessInterface
{
    // repository used to perform database operations
    @Autowired
    ProductRepository repository;

    /**
     * @see ProductBusinessInterface
     * @param product the product to stock
     * @return ProductBusinessResponse an enumeration value indicating the outcome of the operation
     */
    @Override
    public ProductBusinessResponse stock(ProductModel product)
    {
        // save new product in the database
        repository.save(new ProductEntity(
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getQuantity()
        ));

        // indicate a successful operation
        return ProductBusinessResponse.STOCKED;
    }

    /**
     * @see ProductBusinessInterface
     * @param product the product to retrieve
     * @return ProductModel | null The requested product or null if the product wasn't found
     */
    @Override
    public ProductModel retrieveOne(ProductModel product)
    {
        // read the requested product from the database
        Optional<ProductEntity> productEntity = repository.findById(product.getId());

        // if the requested product isn't in the database
        if (!productEntity.isPresent())
        {
            // indicate the product can't be found
            return null;
        }

        // return a product model representing the requested product
        // DO NOT return product entity as that violates layered architecture
        return new ProductModel(
            productEntity.get().getId(),
            productEntity.get().getName(),
            productEntity.get().getDescription(),
            productEntity.get().getPrice(),
            productEntity.get().getQuantity()
        );
    }

    /**
     * @see ProductBusinessInterface
     * @return List all products in the inventory
     */
    @Override
    public List<ProductModel> retrieveAll()
    {
        // initialize List of product models to return
        List<ProductModel> productModels = new ArrayList<>();
        // read all products from the database
        List<ProductEntity> productEntities = repository.findAll();

        // iterate over products
        for(ProductEntity productEntity : productEntities)
        {
            // instantiate product model per product and store in list to return
            // NOTE doing this follows layered architecture
            productModels.add(new ProductModel(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getQuantity()
            ));
        }

        // return all products as product models
        return productModels;
    }

    /**
     * @see ProductBusinessInterface
     * @param product The product to correct with the corrections to be made
     * @return ProductBusinessResponse an enumeration value indicating the outcome of the operation
     */
    @Override
    public ProductBusinessResponse correct(ProductModel product)
    {
        // check if requested product absent from the database
        if (!repository.findById(product.getId()).isPresent())
        {
            // requested product not found - indicate this outcome
            return ProductBusinessResponse.NOT_FOUND;
        }

        // save the corrected product in the database
        repository.save(new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        ));

        // indicate that the product information  was successfully corrected
        return ProductBusinessResponse.CORRECTED;
    }

    /**
     * @see ProductBusinessInterface
     * @param product the product to unstock
     * @return ProductBusinessResponse an enumeration value indicating the outcome of the operation
     */
    @Override
    public ProductBusinessResponse unstock(ProductModel product)
    {
        // check if requested product absent from the database
        if (!repository.findById(product.getId()).isPresent())
        {
            // requested product not found - indicate this outcome
            return ProductBusinessResponse.NOT_FOUND;
        }

        // delete requested product from the database
        repository.deleteById(product.getId());
        // indicate that the requested product was deleted successfully
        return ProductBusinessResponse.UNSTOCKED;
    }
}
