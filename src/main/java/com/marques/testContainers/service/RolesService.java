package com.marques.testContainers.service;

import com.marques.testContainers.domain.Roles;
import com.marques.testContainers.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RolesRepository rolesRepository;

    public Roles getRoleByName(String name) throws BadRequestException {
        Optional<Roles> role = rolesRepository.findByName(name);
        if (role.isEmpty()) {
            throw new BadRequestException("This role does not exists");
        }
        return role.get();
    }

}
