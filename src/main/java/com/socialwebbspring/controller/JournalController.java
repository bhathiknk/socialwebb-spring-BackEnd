package com.socialwebbspring.controller;

import com.socialwebbspring.dto.JournalDto;
import com.socialwebbspring.model.Journal;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.AuthenticationService;
import com.socialwebbspring.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

// JournalController.java
@RestController
@RequestMapping("/journals")
public class JournalController {
    private final JournalService journalService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping("/create")
    public ResponseEntity<Journal> createJournal(@RequestBody JournalDto journalDto,
                                                 @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        User loggedInUser = authenticationService.getUser(token);

        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        journalDto.setUserId(loggedInUser.getId());

        Journal savedJournal = journalService.saveJournal(journalDto);

        try {
            // Generate PDF content and save it to a file
            journalService.generateTextFileContent(savedJournal, "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/Journal PDF/" + savedJournal.gettextFileName());
        } catch (IOException e) {
            // Handle the exception, e.g., log it or return an error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(savedJournal, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Journal>> getUserJournalEntries(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        User loggedInUser = authenticationService.getUser(token);

        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Journal> userJournalEntries = journalService.getUserJournalEntries(loggedInUser.getId());
        return new ResponseEntity<>(userJournalEntries, HttpStatus.OK);
    }


    @GetMapping("/content/{textFileName}")
    public ResponseEntity<String> getJournalContent(@PathVariable String textFileName) {
        try {
            String content = journalService.getJournalContent(textFileName);
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
