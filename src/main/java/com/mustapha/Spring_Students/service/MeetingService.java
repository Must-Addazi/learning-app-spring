package com.mustapha.Spring_Students.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeetingService {
    public String generateMeetingLink() {
        return "https://meet.jit.si/" + UUID.randomUUID();
    }
}