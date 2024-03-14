package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.entities.Role;
import com.project.teachmteachybackend.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getAllRoles(){
        return roleService.findAllRoles();
    }

    @PostMapping
    public Role createRole(@RequestBody Role role){
        return roleService.saveRole(role);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role role){
        return roleService.updateRole(roleId, role).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Role> deleteRole(@PathVariable Long roleId){
        boolean isDeleted = roleService.deleteRole(roleId);
        if(isDeleted)
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}
