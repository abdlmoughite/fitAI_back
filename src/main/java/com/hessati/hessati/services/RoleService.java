package com.hessati.hessati.services;

import com.hessati.hessati.dto.RoleDTO;
import com.hessati.hessati.entities.Role;
import com.hessati.hessati.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        return roleRepository.save(role);
    }

    public Role getRoleById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.orElse(null);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role updateRole(Long id, RoleDTO roleDTO) {
        Optional<Role> existingRoleOptional = roleRepository.findById(id);
        if (existingRoleOptional.isPresent()) {
            Role existingRole = existingRoleOptional.get();
            existingRole.setRoleName(roleDTO.getRoleName());
            return roleRepository.save(existingRole);
        }
        return null;
    }

    public boolean deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
