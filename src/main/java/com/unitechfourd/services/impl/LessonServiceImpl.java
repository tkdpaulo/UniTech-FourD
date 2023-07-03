package com.unitechfourd.services.impl;

import com.unitechfourd.models.Lesson;
import com.unitechfourd.repositories.LessonRepository;
import com.unitechfourd.services.LessonService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
    @Override
    public Lesson save(Lesson lesson) {
        // Salva a lição usando o repositório
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson findById(String id) {
        // Busca a lição pelo ID usando o repositório
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public List<Lesson> findByTeacherId(String teacherId) {
        // Busca as lições pelo ID do professor usando o repositório
        return lessonRepository.findByTeacherId(teacherId);
    }
}
