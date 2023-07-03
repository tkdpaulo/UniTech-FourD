package com.unitechfourd.services.impl;

import com.unitechfourd.exceptions.ResourceNotFoundException;
import com.unitechfourd.models.Teacher;
import com.unitechfourd.repositories.TeacherRepository;
import com.unitechfourd.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher save(Teacher teacher) {
        // Define o status do professor como PENDING antes de salvar
        teacher.setStatus(Teacher.TeacherStatus.PENDING);
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher findById(String id) {
        // Busca o professor pelo ID usando o repositório
        return teacherRepository.findById(id).orElse(null);
    }

    @Override
    public List<Teacher> getAll() {
        // Retorna todos os professores encontrados no banco de dados
        return teacherRepository.findAll();
    }

    @Override
    public Teacher approve(String id, boolean approve) {
        // Busca o professor pelo ID usando o repositório, ou lança uma exceção se não for encontrado
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // Define o status do professor e o campo 'approved' com base no valor do parâmetro 'approve'
        if (approve) {
            teacher.setStatus(Teacher.TeacherStatus.APPROVED);
            teacher.setApproved(true);
        } else {
            teacher.setStatus(Teacher.TeacherStatus.REJECTED);
            teacher.setApproved(false);
        }

        // Salva as alterações do professor no banco de dados
        return teacherRepository.save(teacher);
    }

    public void insertAdminUser() {
        if (teacherRepository.findByUsernameIgnoreCase("admin").isEmpty()) {
            // Cria um novo professor com as informações do administrador
            Teacher teacher = new Teacher();
            teacher.setName("admin");
            teacher.setUsername("admin");
            teacher.setPassword(new BCryptPasswordEncoder().encode("admin"));
            teacher.setStatus(Teacher.TeacherStatus.APPROVED);
            teacher.setApproved(true);
            teacher.setAdmin(true);
            teacher.setAuthorities(List.of(new SimpleGrantedAuthority("ADMIN")));

            // Salva o professor no banco de dados
            save(teacher);
        } else {
            log.info("Admin user already exists!");
        }
    }
}
