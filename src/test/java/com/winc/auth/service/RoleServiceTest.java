package com.winc.auth.service;

import com.winc.auth.entity.Role;
import com.winc.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {

    public static final String readOnly = "readOnly";

    RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleService();
    }

    @Test
    void create_role_fails_if_role_already_exists() {
        roleService.createRole(readOnly);

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(readOnly));
        assertEquals("Role readOnly already exists", e.getMessage());
    }

    @Test
    void create_role_with_role_name() {
        Role role = roleService.createRole(readOnly);

        assertEquals(readOnly, role.getRoleName());
    }

    @Test
    void delete_role_fails_if_role_does_not_exist() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.deleteRole(new Role(readOnly)));
        assertEquals("Role readOnly does not exist", e.getMessage());
    }

    @Test
    void delete_existing_role() {
        Role role = roleService.createRole(readOnly);

        roleService.deleteRole(role);

        assertFalse(roleService.exists(role));
    }

    @Test
    void add_role_to_user_fails_if_role_does_not_exist() {
        User user = new User("dummy", null);

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.addRoleToUser(user, new Role(readOnly)));
        assertEquals("Role readOnly does not exist", e.getMessage());
    }

    @Test
    void add_role_to_user_allowed_for_already_associated_role() {
        User user = new User("dummy", null);

        Role readOnlyRole = roleService.createRole(readOnly);
        Role adminRole = roleService.createRole("admin");

        roleService.addRoleToUser(user, readOnlyRole);
        roleService.addRoleToUser(user, adminRole);
        roleService.addRoleToUser(user, readOnlyRole);

        assertEquals(
                new HashSet<>(asList(readOnlyRole, adminRole)),
                user.getRoles());
    }
}