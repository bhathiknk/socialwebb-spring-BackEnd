package com.socialwebbspring.service;


import com.socialwebbspring.dto.ModuleDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.ModuleEntity;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.ModuleRepository;
import com.socialwebbspring.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ModuleService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private TokenRepository tokenRepository;

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


    public List<ModuleEntity> getAllModules(String token) throws AuthenticationFailException {
        // Authenticate the user
        authenticationService.authenticate(token);

        // Extract user ID from token
        User user = authenticationService.getUser(token);
        if (user == null) {
            throw new AuthenticationFailException("User not found");
        }

        // Retrieve all modules associated with the user
        return moduleRepository.findByUserId(user.getId());
    }
}
