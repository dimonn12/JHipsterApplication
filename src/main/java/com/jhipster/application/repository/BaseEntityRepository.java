package com.jhipster.application.repository;

import com.jhipster.application.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BaseEntityRepository<E extends BaseEntity<E, ID>, ID extends Serializable>
    extends JpaRepository<E, ID> {

}
