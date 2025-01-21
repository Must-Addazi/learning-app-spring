package com.mustapha.Spring_Students.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mustapha.Spring_Students.dtos.*;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.service.ProgramService;
import com.mustapha.Spring_Students.service.RespoProgramService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class ProgramRestController {
    private ProgramService programService;
    private RespoProgramService respoProgramService;
    @GetMapping("/program")
    public List<ProgramDTO> programDTOList(){
        return programService.getPrograms();
    }
    @GetMapping("/programById/{id}")
    public ProgramDTO programDTO( @PathVariable(name = "id") String id) throws ProgramNotFoundException {
        return programService.getProgram(id);
    }
    @PostMapping(value = "/saveProgram", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProgramDTO saveStudentDTO(@RequestParam("poster") MultipartFile file, NewProgram newProgram) throws IOException, ResponsibleProgramNotFoundException {
        ResponsibleProgramDTO responsibleProgramDTO = respoProgramService.getRespoProgram(newProgram.getRespId());
        ProgramDTO programDTO= ProgramDTO.builder().price(newProgram.getPrice())
                .name(newProgram.getName())
                .responsibleProgramDTO(responsibleProgramDTO).build();
        return programService.saveProgram(file,programDTO);
    }
    @GetMapping("/getFile/{programId}/{file}")
    public ResponseEntity<byte[]> getFile(@PathVariable String programId, @PathVariable String file) {
        try {
            byte[] fileBytes = programService.getFile(programId,file);
            ProgramDTO programDTO= programService.getProgram(programId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(programDTO.getName()+ ".pdf").build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ProgramNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/deleteProgram/{id}")
    public Boolean deleteProgram(@PathVariable String id) throws StudentNotFoundException, IOException, PaymentNotFoundException, ProgramNotFoundException {
        log.info("programID "+id);
        return programService.deleteProgram(id);
    }
    @PutMapping("/updateProgram")
    public ProgramDTO updateProgram(
            @RequestParam(value = "posterFile", required = false) MultipartFile posterFile,
           @RequestParam(value = "timingFile", required = false) MultipartFile timingFile,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "price") Double price,
            @RequestParam(value = "posterFileName") String posterFileName,
            @RequestParam(value = "timingFileName") String timingFileName,
            @RequestParam(value = "responsibleProgramDTO.id") String responsibleId,
            @RequestParam(value = "responsibleProgramDTO.name") String responsibleName,
            @RequestParam(value = "responsibleProgramDTO.phoneNumber") String responsiblePhoneNumber,
            @RequestParam(value = "responsibleProgramDTO.email") String responsibleEmail
    ) throws IOException, ProgramNotFoundException {
        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setId(id);
        programDTO.setName(name);
        programDTO.setPrice(price);
        programDTO.setPosterFile(posterFileName);
        programDTO.setTiming(timingFileName);

        ResponsibleProgramDTO responsibleProgramDTO = new ResponsibleProgramDTO();
        responsibleProgramDTO.setId(responsibleId);
        responsibleProgramDTO.setName(responsibleName);
        responsibleProgramDTO.setPhoneNumber(responsiblePhoneNumber);
        responsibleProgramDTO.setEmail(responsibleEmail);

        programDTO.setResponsibleProgramDTO(responsibleProgramDTO);
        return programService.updateProgram(programDTO, posterFile, timingFile);
    }

}
