package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.service.MeetingService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin("*")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/create")
    public ResponseEntity<Map<String,String>> createMeeting() {
        String meetingLink = meetingService.generateMeetingLink();
        Map<String, String> response = new HashMap<>();
        response.put("meetUrl", meetingLink);
        System.out.println("link "+meetingLink);
        return ResponseEntity.ok(response);
    }
}
