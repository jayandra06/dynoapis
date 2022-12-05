package com.dynoapis.dams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dynoapis.dams.entity.ErrorEntity;

public interface ErrorRepository extends JpaRepository<ErrorEntity, Long> {

}
