package com.appsdeveloperblog.app.ws.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}
