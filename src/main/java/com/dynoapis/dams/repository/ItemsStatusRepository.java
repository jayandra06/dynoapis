package com.dynoapis.dams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dynoapis.dams.entity.ItemsStatusEntity;

public interface ItemsStatusRepository extends JpaRepository<ItemsStatusEntity, Long> {
    
    List<ItemsStatusEntity> findByRestaurantId(String resId);

    ItemsStatusEntity findByRestaurantIdAndAggregator(String resId, String aggregator);

}
