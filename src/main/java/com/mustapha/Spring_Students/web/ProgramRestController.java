package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.*;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;
import com.mustapha.Spring_Students.service.ProgramService;
import com.mustapha.Spring_Students.service.RespoProgramService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/program/{id}")
    public ProgramDTO programDTO( @PathVariable(name = "id") String id) throws ProgramNotFoundException {
        return programService.getProgram(id);
    }
    @PostMapping(value = "/saveProgram", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProgramDTO saveStudentDTO(@RequestParam("timing") MultipartFile file, NewProgram newProgram) throws IOException, ProgramNotFoundException, ResponsibleProgramNotFoundException {
        ResponsibleProgramDTO responsibleProgramDTO = respoProgramService.getRespoProgram(newProgram.getRespId());
        ProgramDTO programDTO= ProgramDTO.builder().price(newProgram.getPrice())
                .name(newProgram.getName())
                .responsibleProgramDTO(responsibleProgramDTO).build();
        return programService.saveProgram(file,programDTO);
    }
    @GetMapping("/timingFile/{programId}")
    public ResponseEntity<byte[]> getPaymentFile(@PathVariable String programId) {
        try {
            byte[] fileBytes = programService.getTimingFile(programId);
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
}
