package com.dynoapis.dams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dynoapis.dams.entity.OrderHistoryEntity;

public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, Long> {
    
    OrderHistoryEntity findByRestaurantId(String resId);

}
