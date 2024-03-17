package com.socialwebbspring.service;

import com.socialwebbspring.dto.ModuleQuestionDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.AuthenticationToken;
import com.socialwebbspring.model.ModuleQuestion;
import com.socialwebbspring.repository.ModuleQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ModuleQuestionService {

    @Autowired
    private ModuleQuestionRepository moduleQuestionRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public void saveModuleQuestion(String token, ModuleQuestionDTO moduleQuestionDTO) throws AuthenticationFailException {
        authenticationService.authenticate(token);

        AuthenticationToken authToken = authenticationService.getTokenByToken(token);
        Integer userId = authToken.getUserIdFromToken();

        ModuleQuestion moduleQuestion = new ModuleQuestion();
        moduleQuestion.setModuleId(moduleQuestionDTO.getModuleId());
        moduleQuestion.setUserId(userId);
        moduleQuestion.setModuleQuestion(moduleQuestionDTO.getModuleQuestion());
        moduleQuestion.setModuleAnswer(moduleQuestionDTO.getModuleAnswer());

        moduleQuestionRepository.save(moduleQuestion);
    }

    public List<ModuleQuestion> getModuleQuestionsByUserIdAndModuleId(String token, Integer moduleId) throws AuthenticationFailException {
        authenticationService.authenticate(token);

        AuthenticationToken authToken = authenticationService.getTokenByToken(token);
        Integer userId = authToken.getUserIdFromToken();

        return moduleQuestionRepository.findByUserIdAndModuleId(userId, moduleId);
    }
}
