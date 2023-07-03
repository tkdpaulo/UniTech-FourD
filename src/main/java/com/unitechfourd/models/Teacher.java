package com.unitechfourd.models;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Data
public class Teacher {
    private String id;
    private String name;
    private String username;
    private String password;
    private TeacherStatus status;
    private boolean approved;
    private boolean admin = false;
    private Collection<SimpleGrantedAuthority> authorities;
    public enum TeacherStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
