package com.farm.service;

import com.farm.entity.RestaurantMaterial;
import com.farm.repository.RestaurantMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RestaurantMaterialService {

    @Autowired
    private RestaurantMaterialMapper restaurantMaterialMapper;

    public void save(RestaurantMaterial restaurantMaterial) {

        if (restaurantMaterial.getId() != 0) {
            restaurantMaterialMapper.updateByPrimaryKey(restaurantMaterial);
        } else {
            restaurantMaterialMapper.insert(restaurantMaterial);
        }
    }

    public List<RestaurantMaterial> findAll() {

        return restaurantMaterialMapper.findAll();
    }

    public RestaurantMaterial findByName(String name) {

        return restaurantMaterialMapper.findByName(name);
    }

    public void delete(RestaurantMaterial restaurantMaterial) {

        restaurantMaterialMapper.deleteByPrimaryKey(restaurantMaterial.getId());
    }
}
