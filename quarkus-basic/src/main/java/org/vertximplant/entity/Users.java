package org.vertximplant.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity
@Cacheable
public class Users extends PanacheEntity {

    @Column(length = 64,unique = true)
    public String name;


}
