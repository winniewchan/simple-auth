package com.winc.auth;

import com.winc.auth.entity.Role;
import com.winc.auth.entity.User;
import com.winc.auth.service.AuthTokenService;
import com.winc.auth.service.RoleService;
import com.winc.auth.service.UserService;
import com.winc.auth.service.util.AuthClock;
import com.winc.auth.service.util.PasswordHasher;
import com.winc.auth.service.util.TokenGenerator;

import java.time.Clock;

public class Demo {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthTokenService authTokenService;

    public static void main(String[] args) {
        new Demo().happyPath();
    }

    public Demo() {
        // manual dependency injection
        PasswordHasher passwordHasher = new PasswordHasher();
        TokenGenerator tokenGenerator = new TokenGenerator();
        AuthClock authClock = new AuthClock(Clock.systemUTC());

        this.userService = new UserService(passwordHasher);
        this.roleService = new RoleService();
        this.authTokenService = new AuthTokenService(userService, tokenGenerator, authClock);
    }

    private void happyPath() {
        System.out.println("Creating user...");
        User user = userService.createUser("HelloWorld", "foobar");
        System.out.println("User " + user.getUsername() + " created.");
        System.out.println();

        System.out.println("Creating role...");
        Role viewer = roleService.createRole("viewer");
        System.out.println("Role " + viewer.getRoleName() + " created.");
        Role contributor = roleService.createRole("contributor");
        System.out.println("Role " + contributor.getRoleName() + " created.");
        System.out.println();

        System.out.println("Adding role to user...");
        roleService.addRoleToUser(user, viewer);
        System.out.println("User " + user.getUsername() + " now has roles " + user.getRoles());
        System.out.println();

        System.out.println("Authenticating user...");
        String token = authTokenService.authenticate(user.getUsername(), "foobar");
        System.out.println("User " + user.getUsername() + " authenticated.");
        System.out.println();

        System.out.println("Getting all roles by token...");
        System.out.println("All roles: " + authTokenService.allRoles(token));
        System.out.println();

        System.out.println("Adding another role to user...");
        roleService.addRoleToUser(user, contributor);
        System.out.println("User " + user.getUsername() + " now has roles " + user.getRoles());
        System.out.println();

        System.out.println("Getting all roles by token again...");
        System.out.println("All roles: " + authTokenService.allRoles(token));
    }
}
