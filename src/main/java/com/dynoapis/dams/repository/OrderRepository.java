package com.dynoapis.dams.repository;


import com.dynoapis.dams.entity.OrderEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long> {

    List<OrderEntity> findByRestaurantIdAndCreatedAtBetween(String resId, Timestamp startDate, Timestamp endDate);
    List<OrderEntity> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

    OrderEntity findByOrderId(String orderId);

    List<OrderEntity> findByRestaurantId(String resId);

    boolean existsByOrderId(String orderId);

}
