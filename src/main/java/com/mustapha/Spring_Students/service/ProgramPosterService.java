package com.mustapha.Spring_Students.service;


import com.mustapha.Spring_Students.dtos.ProgramPosterDTO;
import com.mustapha.Spring_Students.entities.ProgramPoster;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProgramPosterService {
    List<ProgramPosterDTO> getPosters();
    Boolean deletePoster(Long posterId) throws IOException;
    ProgramPoster addPoster(String programId, MultipartFile poster) throws IOException, ProgramNotFoundException;
}
