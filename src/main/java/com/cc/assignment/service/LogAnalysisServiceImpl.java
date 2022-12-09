package com.cc.assignment.service;

import com.cc.assignment.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class LogAnalysisServiceImpl implements LogAnalysisService {

    @Override
    public int analysisFile(InputStream is) {
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            Event event;
            String line;
            while ((line = reader.readLine()) != null) {
                event = new ObjectMapper().readValue(line, Event.class);
                log.info("Read line {}", event.toString());
            }

        } catch (IOException e) {
            log.error("Analysis File exception {}", e.getCause());
        }
        return 0;
    }
}
