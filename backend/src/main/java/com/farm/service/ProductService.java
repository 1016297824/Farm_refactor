package com.farm.service;

import com.farm.entity.Product;
import com.farm.repository.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<Product> findAll() {

        return productMapper.findAll();
    }

    public List<Product> findByProductType(String productType) {

        return productMapper.findByProductType(productType);
    }

    public void save(Product product) {

        if (product.getId() != 0) {
            productMapper.updateByPrimaryKey(product);
        } else {
            productMapper.insert(product);
        }
    }

    public void deleteProduct(Product product) {

        productMapper.deleteByPrimaryKey(product.getId());
    }

    public Product findByName(String name) {

        return productMapper.findByName(name);
    }

    public void initProduct() {

        List<Product> productList = new ArrayList<Product>();

        Product product = new Product("猪", "头", 20.0, 5.0, Product.productType1, 0.0, "头");
        Product product1 = new Product("鸡", "只", 50.0, 10.0, Product.productType1, 0.0, "只");
        Product product2 = new Product("鸭子", "只", 50.0, 10.0, Product.productType1, 0.0, "只");
        Product product3 = new Product("鹅", "只", 50.0, 10.0, Product.productType1, 0.0, "只");
        Product product4 = new Product("兔子", "只", 30.0, 5.0, Product.productType1, 0.0, "只");
        Product product5 = new Product("鹌鹑", "只", 50.0, 10.0, Product.productType1, 0.0, "只");

        Product product6 = new Product("鲫鱼", "斤", 100.0, 20.0, Product.productType2, 0.0, "斤");
        Product product7 = new Product("鲤鱼", "斤", 100.0, 20, Product.productType2, 0.0, "斤");
        Product product8 = new Product("白鲢鱼", "斤", 100.0, 20.0, Product.productType2, 0.0, "斤");

        Product product9 = new Product("苹果", "斤", 100.0, 20.0, Product.productType3, 0.0, "株");
        Product product10 = new Product("枣", "斤", 100.0, 20.0, Product.productType3, 0.0, "株");
        Product product11 = new Product("石榴", "斤", 100.0, 20.0, Product.productType3, 0.0, "株");
        Product product12 = new Product("梨", "斤", 100.0, 20.0, Product.productType3, 0.0, "株");
        Product product13 = new Product("白菜", "斤", 100.0, 20.0, Product.productType3, 0.0, "袋");
        Product product14 = new Product("土豆", "斤", 100.0, 20.0, Product.productType3, 0.0, "斤");

        productList.add(product);
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        productList.add(product5);
        productList.add(product6);
        productList.add(product7);
        productList.add(product8);
        productList.add(product9);
        productList.add(product10);
        productList.add(product11);
        productList.add(product12);
        productList.add(product13);
        productList.add(product14);

        for (Product p : productList) {
            productMapper.insert(p);
        }
    }
}
