package com.dynoapis.dams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dynoapis.dams.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    
    List<ItemEntity> findByRestaurantId(String resId);
    ItemEntity findByRestaurantIdAndEntityId(String resId, String uid);

}
