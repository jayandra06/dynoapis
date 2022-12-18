package com.dynoapis.dams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dynoapis.dams.entity.OrderHistoryEntity;

public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, Long> {
    
    List<OrderHistoryEntity> findByRestaurantId(String resId);

    OrderHistoryEntity findByRestaurantIdAndAggregator(String resId, String aggregator);

}
