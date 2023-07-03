package com.unitechfourd.security.services;

import com.unitechfourd.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        // Busca o usuário no repositório com base no nome de usuário (login)
        final var user = teacherRepository.findByUsernameIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with login: " + login));

        // Cria a lista de autoridades com base no perfil do usuário
        final var authorityListAdmin = AuthorityUtils.createAuthorityList("USER", "ADMIN");
        final var authorityListUser = AuthorityUtils.createAuthorityList("USER");

        // Cria e retorna a instância de UserDetailsImpl com base no usuário e na lista de autoridades
        return UserDetailsImpl.build(user, user.isAdmin() ? authorityListAdmin : authorityListUser);
    }
}
