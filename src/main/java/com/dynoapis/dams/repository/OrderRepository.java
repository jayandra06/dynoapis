package com.dynoapis.dams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dynoapis.dams.entity.OrderEntity;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByResTokenIdAndCreatedAtBetween(String orderId, Timestamp startDate, Timestamp endDate);

}
