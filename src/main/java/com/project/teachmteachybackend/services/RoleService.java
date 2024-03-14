package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.entities.Role;
import com.project.teachmteachybackend.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> updateRole(Long roleId, Role role) {
        return roleRepository.findById(roleId).map(existingRole -> {
           existingRole.setRoleName(role.getRoleName());
           return roleRepository.save(existingRole);
        });
    }

    public boolean deleteRole(Long roleId) {
        return roleRepository.findById(roleId).map(role ->{
            roleRepository.delete(role);
            return true;
        }).orElse(false);
    }
}
