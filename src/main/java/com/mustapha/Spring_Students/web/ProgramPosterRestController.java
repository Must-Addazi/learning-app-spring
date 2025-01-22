package com.mustapha.Spring_Students.web;


import com.mustapha.Spring_Students.dtos.ProgramPosterDTO;
import com.mustapha.Spring_Students.entities.ProgramPoster;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.service.ProgramPosterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class ProgramPosterRestController {
       private ProgramPosterService programPosterService;
        @GetMapping("/posters")
        public List<ProgramPosterDTO> getAllPosters( ){
            return programPosterService.getPosters();
        }
        @PostMapping(value = "/savePoster/{programId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
        public ProgramPoster saveStudentDTO(@RequestParam(value = "poster", required = true) MultipartFile poster,@PathVariable String programId) throws IOException, ProgramNotFoundException {
            return programPosterService.addPoster(programId,poster);
        }
        @DeleteMapping("/deletePoster/{id}")
        @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
        public Boolean deletePoster(@PathVariable Long id) throws IOException {
            return programPosterService.deletePoster(id);
        }
}
