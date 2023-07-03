package com.unitechfourd.controllers;

import com.unitechfourd.models.Lesson;
import com.unitechfourd.security.services.UserDetailsImpl;
import com.unitechfourd.services.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }
    @PostMapping
    public ResponseEntity<Lesson> save(@RequestBody Lesson lesson, Authentication authentication) {
        // Obtém o ID do professor autenticado a partir do objeto de autenticação
        String teacherId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        lesson.setTeacherId(teacherId);
        // Salva a lição e retorna uma resposta bem-sucedida com a lição salva
        return ResponseEntity.ok(lessonService.save(lesson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lesson> update(@PathVariable String id, @RequestBody Lesson lesson, Authentication authentication) {
        // Obtém o ID do professor autenticado a partir do objeto de autenticação
        String teacherId = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        // Verifica se a lição existente com o ID fornecido existe
        Lesson existingLesson = lessonService.findById(id);
        if (existingLesson == null) {
            return ResponseEntity.notFound().build();
        }

        // Injeta novamente o ID fornecido e o ID do professor autenticado
        lesson.setId(id);
        lesson.setTeacherId(teacherId);

        // Salva a lição atualizada e retorna uma resposta bem-sucedida com a lição salva
        return ResponseEntity.ok(lessonService.save(lesson));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<Lesson>> findByProfessorId(@PathVariable String id) {
        // Encontra todas as lições pelo ID do professor fornecido e retorna uma resposta bem-sucedida com a lista de lições encontradas
        return ResponseEntity.ok(lessonService.findByTeacherId(id));
    }

    @GetMapping
    public List<Lesson> getLessons(Authentication authentication) {
        // Obtém o ID do professor autenticado a partir do objeto de autenticação
        String teacherId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        // Retorna todas as lições do professor autenticado
        return lessonService.findByTeacherId(teacherId);
    }
}
