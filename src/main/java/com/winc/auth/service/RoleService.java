package com.winc.auth.service;

import com.winc.auth.entity.Role;
import com.winc.auth.entity.User;

import java.util.HashSet;
import java.util.Set;

public class RoleService {

    private final Set<Role> allRoles = new HashSet<>();

    public Role createRole(String roleName) {
        Role role = new Role(roleName);
        if (exists(role)) {
            throw new IllegalArgumentException("Role " + roleName + " already exists");
        }
        allRoles.add(role);
        return role;
    }

    public void deleteRole(Role role) {
        if (!exists(role)) {
            throw new IllegalArgumentException("Role " + role.getRoleName() + " does not exist");
        }
        allRoles.remove(role);
    }

    public void addRoleToUser(User user, Role role) {
        if (!exists(role)) {
            throw new IllegalArgumentException("Role " + role.getRoleName() + " does not exist");
        }
        user.addRole(role);
    }

    boolean exists(Role role) {
        return allRoles.contains(role);
    }
}
