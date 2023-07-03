package com.unitechfourd.controllers;

import com.unitechfourd.controllers.dto.TeacherResponseDTO;
import com.unitechfourd.models.Teacher;
import com.unitechfourd.security.services.UserDetailsImpl;
import com.unitechfourd.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> save(@RequestBody Teacher teacher) {
        // Criptografa a senha do professor antes de salvar
        teacher.setPassword(new BCryptPasswordEncoder().encode(teacher.getPassword()));
        // Salva o professor e retorna a resposta com o DTO do professor
        return ResponseEntity.ok(TeacherResponseDTO.toDTO(teacherService.save(teacher)));
    }

    @PutMapping
    public ResponseEntity<Teacher> update(@RequestBody Teacher teacher, Authentication authentication) {
        // Obtém o ID do professor autenticado
        String teacherId = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        // Verifica se o professor existe antes de atualizá-lo
        Teacher existingTeacher = teacherService.findById(teacherId);
        if (existingTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        teacher.setId(teacherId);
        // Atualiza o professor e retorna a resposta com o professor atualizado
        return ResponseEntity.ok(teacherService.save(teacher));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TeacherResponseDTO>> getAll() {
        // Obtém todos os professores
        List<Teacher> teachers = teacherService.getAll();
        // Converte a lista de professores para uma lista de DTOs de professores
        List<TeacherResponseDTO> teacherResponseDTOS = teachers.stream()
                .map(TeacherResponseDTO::toDTO)
                .collect(Collectors.toList());
        // Retorna a resposta com a lista de DTOs de professores
        return ResponseEntity.ok(teacherResponseDTOS);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Teacher> approve(@PathVariable String id, @RequestParam boolean approve) {
        // Aprova ou desaprova um professor com base no ID fornecido
        return ResponseEntity.ok(teacherService.approve(id, approve));
    }

}
