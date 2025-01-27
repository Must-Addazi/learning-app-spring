package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ProgramPosterDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.ProgramPoster;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ProgramPosterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ProgramPosterServiceImpl implements ProgramPosterService{
    private ProgramPosterRepository programPosterRepository;
    private ProgramService programService;
    private Mapper mapper;
    @Override
    public List<ProgramPosterDTO> getPosters() {
        List<ProgramPoster> posterList = programPosterRepository.findAll();
        return posterList.stream().map(poster -> {
            ProgramPosterDTO posterDTO = ProgramPosterDTO.builder()
                    .program(mapper.fromProgram(poster.getProgram()))
                    .id(poster.getId())
                    .build();
            String encodedImage = encodeImageToBase64(poster.getUrl());
            posterDTO.setUrl(encodedImage);
            return posterDTO;
        }).toList();
    }


    @Override
    public Boolean deletePoster(Long posterId) throws IOException {
        ProgramPoster programPoster= programPosterRepository.findById(posterId).get();
        String posterPath= programPoster.getUrl();
        if( posterPath != null){
            Path path= Paths.get(URI.create(posterPath));
            Files.deleteIfExists(path);
        }
        programPosterRepository.delete(programPoster);
        return !programPosterRepository.existsById(posterId);
    }

    @Override
    public ProgramPoster addPoster(String programId, MultipartFile poster) throws IOException, ProgramNotFoundException {
        Path path = Paths.get(System.getProperty("user.home"), "students-app-files", "Posters");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Program program = mapper.fromProgramDTO(programService.getProgram(programId));
        ProgramPoster programPoster= ProgramPoster.builder().program(program).build();
        String PosterFileID = program.getName() + programId;
        if (poster != null && (Objects.requireNonNull(poster.getOriginalFilename()).endsWith(".jpg") || poster.getOriginalFilename().endsWith(".png"))) {
            Path imagePath = Paths.get(System.getProperty("user.home"), "students-app-files", "Posters", PosterFileID + getFileExtension(poster));
            Files.copy(poster.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            programPoster.setUrl(imagePath.toUri().toString());
        }
        programPosterRepository.save(programPoster);
            return programPoster;
    }
    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "";
        }
        int dotIndex = originalFilename.lastIndexOf('.');
        return (dotIndex != -1) ? originalFilename.substring(dotIndex) : "";
    }
    private String encodeImageToBase64(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                if (imagePath.startsWith("file:///")) {
                    imagePath = imagePath.substring(7);
                }
                byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
                return Base64.getEncoder().encodeToString(imageBytes);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de l'image: " + e.getMessage());
            return null;
        }
    }
}
