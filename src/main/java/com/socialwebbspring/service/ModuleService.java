package com.socialwebbspring.service;


import com.socialwebbspring.dto.ModuleDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.ModuleEntity;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.ModuleQuestionRepository;
import com.socialwebbspring.repository.ModuleRepository;
import com.socialwebbspring.repository.TokenRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ModuleQuestionRepository moduleQuestionRepository;

    public void saveModule(String token, ModuleDTO moduleDTO) throws AuthenticationFailException {
        // Authenticate the user
        authenticationService.authenticate(token);

        // Extract user ID from token
        User user = authenticationService.getUser(token);
        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("User not found");
        }

        // Create a new ModuleEntity object
        ModuleEntity module = new ModuleEntity();
        module.setModuleName(moduleDTO.getModuleName());
        module.setUserId(user.getId());

        // Save the module
        moduleRepository.save(module);
    }


    public List<ModuleDTO> getAllModules(String token) throws AuthenticationFailException {
        // Authenticate the user
        authenticationService.authenticate(token);

        // Extract user ID from token
        User user = authenticationService.getUser(token);
        if (user == null) {
            throw new AuthenticationFailException("User not found");
        }

        // Retrieve all modules associated with the user
        List<ModuleEntity> modules = moduleRepository.findByUserId(user.getId());

        // Convert ModuleEntity objects to ModuleDTO
        List<ModuleDTO> moduleDTOs = modules.stream()
                .map(module -> {
                    ModuleDTO moduleDTO = new ModuleDTO();
                    moduleDTO.setId(module.getId());
                    moduleDTO.setModuleName(module.getModuleName());
                    return moduleDTO;
                })
                .collect(Collectors.toList());

        return moduleDTOs;
    }



    @Transactional
    public void deleteModuleAndQuestionsById(String token, Integer moduleId) throws AuthenticationFailException {
        authenticationService.authenticate(token);

        // Delete module questions first
        moduleQuestionRepository.deleteByModuleId(moduleId);

        // Then delete the module
        moduleRepository.deleteById(moduleId);
    }


}
