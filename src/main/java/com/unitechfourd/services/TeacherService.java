package com.unitechfourd.services;

import com.unitechfourd.models.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeacherService {
    Teacher save(Teacher teacher);
    Teacher findById(String id);
    List<Teacher> getAll();
    Teacher approve(String id, boolean approve);
    void insertAdminUser();
}
