package com.navv.model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.navv.model.Permission.*;


@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),

    ADMIN(

            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_CREATE,
                    ADMIN_DELETE



            )
    ),



    DRIVER(
            Set.of(
                    DRIVER_READ,
                    DRIVER_UPDATE,
                    DRIVER_CREATE,
                    DRIVER_DELETE


            )
    ),


    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_CREATE,
                    MANAGER_DELETE


            )
    ),

    ;
    @Getter
    private final Set<Permission> permission;

    public List<SimpleGrantedAuthority>getAuthorites(){
        var authorites=   getPermission()
                .stream()
                .map(permissions1 -> new SimpleGrantedAuthority(permissions1.getPermission()))
                .collect(Collectors.toList());
        authorites.add(new SimpleGrantedAuthority("ROLE_"+this.name()));

        return authorites;

    }
}