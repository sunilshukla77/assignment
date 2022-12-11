package com.cc.assignment.service;

import com.cc.assignment.dao.LogAnalysisRepository;
import com.cc.assignment.entity.EventDetail;
import com.cc.assignment.model.Event;
import com.cc.assignment.model.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


@Slf4j
@Service
public class LogAnalysisServiceImpl implements LogAnalysisService {

    @Autowired
    private LogAnalysisRepository logAnalysisRepository;

    public LogAnalysisServiceImpl(LogAnalysisRepository logAnalysisRepository) {
        this.logAnalysisRepository = logAnalysisRepository;
    }

    @Override
    public int analysisFile(InputStream is) {
        Map<String, Event> eventMap = new HashMap<>();
        Map<String, EventDetail> eventDetailMap = new HashMap<>();
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            Event event;
            String line;
            while ((line = reader.readLine()) != null) {
                event = new ObjectMapper().readValue(line, Event.class);
                log.info("Read line {}", event.toString());
                parseEvent(eventMap, event, eventDetailMap);
            }
            if (eventDetailMap.size() > 0) {
                persistAlerts(eventDetailMap.values());
            }
        } catch (JsonProcessingException e) {
            log.error("Unable to parse the event! {}", e.getMessage());
        } catch (IOException e) {
            log.error("Unable to access the File {}", e.getMessage());
        }
        return eventDetailMap.size();
    }

    private void parseEvent(Map<String, Event> eventMap, Event event, Map<String, EventDetail> eventDetailMap) {
        if(eventMap.containsKey(event.getId())){
            Event e1 = eventMap.get(event.getId());
            long executionTime = getEventExecutionTime(event, e1);
            EventDetail eventDetail = new EventDetail(event, Math.toIntExact(executionTime));
            if (executionTime > 4L) {
                eventDetail.setAlert(Boolean.TRUE);
                log.info("!!! Execution time for the event {} is {}ms", event.getId(), executionTime);
            }
            eventDetailMap.put(event.getId(), eventDetail);
            eventMap.remove(event.getId());
            log.info("Remove event id : {}", event);
        }else{
            eventMap.put(event.getId(), event);
            log.info("Add event id : {}", event.getId());
        }
    }

    private void persistAlerts(Collection<EventDetail> values) {
        log.debug("Persisting {} alerts...", values.size());
        logAnalysisRepository.saveAll(values);
    }

    private long getEventExecutionTime(Event event1, Event event2) {
        Event endEvent = Stream.of(event1, event2).filter(e -> State.FINISHED.equals(e.getState())).findFirst().orElse(null);
        Event startEvent = Stream.of(event1, event2).filter(e -> State.STARTED.equals(e.getState())).findFirst().orElse(null);

        return Objects.requireNonNull(endEvent).getTimestamp() - Objects.requireNonNull(startEvent).getTimestamp();
    }
    
    
}

