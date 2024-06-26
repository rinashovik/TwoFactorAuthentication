package com.example.TwoFactorAuthentication.dao;

import ch.qos.logback.core.model.INamedModel;
import com.example.TwoFactorAuthentication.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends JpaRepository<Role, INamedModel> {
   // Role findByRole(String user);
    Role findByRole(String name);

}
