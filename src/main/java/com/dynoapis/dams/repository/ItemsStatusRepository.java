package com.dynoapis.dams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dynoapis.dams.entity.ItemsStatusEntity;

public interface ItemsStatusRepository extends JpaRepository<ItemsStatusEntity, Long> {

    ItemsStatusEntity findByRestaurantId(String resId);

}
