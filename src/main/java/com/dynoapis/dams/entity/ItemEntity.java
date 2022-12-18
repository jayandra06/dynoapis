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
@Table(name="tbl_items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_response", columnDefinition = "TEXT")
    private String statusResponse;
    
    @Column(name = "restaurant_id")
    private String restaurantId;

    @Column(name = "entity_id")
    private String entityId;

    private String aggregator;

    @Column(name = "stock_status")
    private Boolean isStatus;

    @Column(name = "is_processed")
    private Boolean isProcessed;

    private String type;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
