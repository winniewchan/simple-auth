package com.winc.auth.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void user_created_with_no_roles_by_default() {
        User user = new User("dummy", null);

        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void add_role_to_user() {
        User user = new User("dummy", null);
        Role role1 = new Role("role1");

        user.addRole(role1);

        assertEquals(singleton(role1), user.getRoles());
    }

    @Test
    void get_roles_return_a_copy_of_all_roles() {
        User user = new User("dummy", null);
        user.addRole(new Role("role1"));
        user.addRole(new Role("role2"));

        Set<Role> copy = user.getRoles();
        copy.add(new Role("role3"));

        assertEquals(2, user.getRoles().size());
        assertEquals(3, copy.size());
    }
}