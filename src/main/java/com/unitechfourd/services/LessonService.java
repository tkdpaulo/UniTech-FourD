package com.unitechfourd.services;

import com.unitechfourd.models.Lesson;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface LessonService {
    Lesson save(Lesson lesson);
    Lesson findById(String id);
    List<Lesson> findByTeacherId(String id);
}
