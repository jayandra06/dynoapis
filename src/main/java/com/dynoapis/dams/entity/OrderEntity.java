package com.dynoapis.dams.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name="tbl_orders")
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_json", columnDefinition = "TEXT")
    private String orderJson;

    @Column(name = "status_response_json", columnDefinition = "TEXT")
    private String statusResponse;

    @Column(name = "order_id", unique=true)
    private String orderId;

    private String vendor;

    private String status;

    private int statusCode;

    @Column(name = "res_id")
    private String restaurantId;

    @Column(name = "is_processed")
    private boolean isProcessed;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

}
