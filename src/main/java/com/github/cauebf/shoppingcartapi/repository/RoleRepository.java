package com.github.cauebf.shoppingcartapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.cauebf.shoppingcartapi.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Object findByName(String string);
}
