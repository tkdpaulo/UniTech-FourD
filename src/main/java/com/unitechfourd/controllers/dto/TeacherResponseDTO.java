package com.unitechfourd.controllers.dto;

import com.unitechfourd.models.Teacher;
import lombok.Data;

@Data
public class TeacherResponseDTO{
    private String id;
    private String name;
    private String username;
    private Teacher.TeacherStatus status;
    private boolean approved;
    private boolean admin;

    public TeacherResponseDTO(String id, String name, String username, Teacher.TeacherStatus status, boolean approved, boolean admin) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.status = status;
        this.approved = approved;
        this.admin = admin;
    }

    public static TeacherResponseDTO toDTO(Teacher teacher) {
        return new TeacherResponseDTO(teacher.getId(), teacher.getName(), teacher.getUsername(), teacher.getStatus(), teacher.isApproved(), teacher.isAdmin());
    }
}
