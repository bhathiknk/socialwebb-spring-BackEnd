package com.socialwebbspring.service;

import com.socialwebbspring.dto.JournalDto;
import com.socialwebbspring.model.Journal;
import com.socialwebbspring.repository.JournalRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class JournalService {

    private final JournalRepository journalRepository;

    @Value("${C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/Journal PDF/}")
    private String textStoragePath;

    @Autowired
    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public Journal saveJournal(JournalDto journalDto) {
        // Create a new Journal entity from the DTO
        Journal journal = new Journal();
        journal.setDate(journalDto.getDate());
        journal.setMood(journalDto.getMood());
        journal.setGoals(journalDto.getGoals());

        // Set the userId
        journal.setUserId(journalDto.getUserId());

        // Save the entity to the database
        Journal savedJournal = journalRepository.save(journal);

        // Generate and save text file reference name
        generateAndSaveTextFile(savedJournal);

        return savedJournal;
    }

    private void generateAndSaveTextFile(Journal journal) {
        try {
            String fileName = UUID.randomUUID().toString() + ".txt";
            String filePath = StringUtils.cleanPath(textStoragePath + "/" + fileName);

            // Save the text file reference name to the journal entity
            journal.settextFileName(fileName);

            // Update the entity in the database with the text file reference name
            journalRepository.save(journal);

            // Generate and save the actual text content
            generateTextFileContent(journal, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }

    public void generateTextFileContent(Journal journal, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write("<p><strong>Journal Entry:</strong> Date: " + journal.getDate() + "</p>");
            fileWriter.write("<p><strong>Mood:</strong> " + journal.getMood() + "</p>");
            fileWriter.write("<p><strong>Goals:</strong> " + journal.getGoals() + "</p>");
            fileWriter.write("<p><strong>Content:</strong></p>");

            // Use Jsoup to clean HTML and get plain text
            String plainTextContent = Jsoup.clean(journal.getContent(), Whitelist.none());

            // Write plain text content
            fileWriter.write("<div>" + plainTextContent + "</div>");
        }
    }

    // JournalService.java
    public List<Journal> getUserJournalEntries(Integer userId) {
        return journalRepository.findByUserId(userId);
    }



    public String getJournalContent(String textFileName) throws IOException {
        String filePath = StringUtils.cleanPath(textStoragePath + "/" + textFileName);
        return Files.readString(Paths.get(filePath));
    }

// Inside JournalService class

    public Journal getJournalById(Integer id) {
        return journalRepository.findById(id).orElse(null);
    }

    public void deleteJournalAndTextFile(Journal journal) throws IOException {
        // Delete the text file associated with the journal entry
        deleteTextFile(journal);

        // Delete the journal entry from the database
        journalRepository.delete(journal);
    }

    private void deleteTextFile(Journal journal) throws IOException {
        String textFileName = journal.gettextFileName();
        if (textFileName != null) {
            String filePath = StringUtils.cleanPath(textStoragePath + "/" + textFileName);
            Files.deleteIfExists(Paths.get(filePath));
        }
    }


}
