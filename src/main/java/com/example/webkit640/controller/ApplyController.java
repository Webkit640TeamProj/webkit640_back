package com.example.webkit640.controller;

import com.example.webkit640.dto.ApplicantDTO;
import com.example.webkit640.dto.ApplyDTO;
import com.example.webkit640.dto.ClientApplicantDTO;
import com.example.webkit640.dto.FileDTO;
import com.example.webkit640.dto.request.SearchApplicantRequestDTO;
import com.example.webkit640.dto.request.SelectApplicantDTO;
import com.example.webkit640.dto.request.ApplyForcedSelectRequestDTO;
import com.example.webkit640.dto.response.ApplicantDataResponseDTO;
import com.example.webkit640.dto.response.ResponseDTO;
import com.example.webkit640.dto.response.SaveTraineeResponseDTO;
import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.entity.Member;
import com.example.webkit640.entity.Trainee;
import com.example.webkit640.service.ApplyService;
import com.example.webkit640.service.FileService;
import com.example.webkit640.service.MemberService;
import com.example.webkit640.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/apply")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = {"Content-Disposition"})
public class ApplyController {
    private final FileService fileService;
    private final ApplyService applyService;
    private final MemberService memberService;
    private final ResourceLoader resourceLoader;
    private final TraineeService traineeService;


    @Autowired
    public ApplyController(FileService fileService, ApplyService applyService, MemberService memberService,
                           ResourceLoader resourceLoader, TraineeService traineeService) {
        this.fileService = fileService;
        this.applyService = applyService;
        this.memberService = memberService;
        this.resourceLoader = resourceLoader;
        this.traineeService = traineeService;
    }

    @PostMapping("/applicant-data")
    public ResponseEntity<?> applicantDataSave(@RequestBody ApplyDTO applyDTO, @AuthenticationPrincipal int id) {
        log.info("ENTER /apply/applicant-data - Accessor : "+memberService.findByid(id).getEmail());
        Member member = memberService.findByid(id);

        Applicant resultApplicant = getResultApplicant(Applicant.builder()
                .name(applyDTO.getName())
                .application(applyDTO.getApplication())
                .member(member)
                .schoolYear(applyDTO.getSchoolYear())
                .major(applyDTO.getMajor())
                .school(applyDTO.getSchool())
                .schoolNum(applyDTO.getSchoolNumber()));
        ApplicantDataResponseDTO dto = ApplicantDataResponseDTO.builder()
                .application(resultApplicant.getApplication())
                .major(resultApplicant.getMajor())
                .name(resultApplicant.getName())
                .school(resultApplicant.getSchool())
                .schoolNumber(resultApplicant.getSchoolNum())
                .schoolYear(resultApplicant.getSchoolYear())
                .build();
        List<ApplicantDataResponseDTO> response = new ArrayList<>();
        response.add(dto);
        log.info("LEAVE /apply/applicant-data - Accessor : "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("applicant-application")
    public ResponseEntity<?> applicantUploadFile(@AuthenticationPrincipal int id,@RequestParam MultipartFile file) throws IOException {
        log.info("ENTER /apply/applicant-application - Accessor: "+memberService.findByid(id).getEmail());
        if (file == null) {
            log.error("FILE ERROR /apply/applicant-application -Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("ERROR");
        }
        if (!file.isEmpty()) {
            Member member = memberService.findByid(id);
            Applicant applicant = applyService.getByMemberId(member.getId());

            FileEntity fileEntity = fileService.saveFile(file,applicant,member);
            List<FileEntity> modifyMemberFile = member.getFile();
            modifyMemberFile.add(fileEntity);
            member.setFile(modifyMemberFile);
            memberService.save(member);

            applicant.setApply(true);
            applyService.saveApplicant(applicant);

            ResponseDTO response = getResponseDTO(modifyMemberFile, member, applicant);
            log.info("LEAVE /apply/applicant-application - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(response);
        } else {
            log.error("ERROR /apply/applicant-application -Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @PostMapping("/applies")
    public ResponseEntity<?> uploadTest(@RequestPart MultipartFile file, @RequestPart ApplyDTO applyDTO, @AuthenticationPrincipal int id) throws IOException {
        log.info(applyDTO.toString());
        Member member = memberService.findByid(id);
        log.info(member.toString());

        Applicant resultApplicant = getResultApplicant(Applicant.builder()
                .name(applyDTO.getName())
                .application(applyDTO.getApplication())
                .isApply(false)
                .isAdminApply(false)
                .member(member)
                .isSelect(false)
                .schoolYear(applyDTO.getSchoolYear())
                .major(applyDTO.getMajor())
                .school(applyDTO.getSchool())
                .schoolNum(applyDTO.getSchoolNumber()));

        FileEntity fileEntity = fileService.saveFile(file,resultApplicant,member);

        List<FileEntity> modifyMemberFile = member.getFile();
        modifyMemberFile.add(fileEntity);
        Member modifyMember = Member.builder()
                .id(member.getId())
                .applicant(resultApplicant)
                .boards(member.getBoards())
                .counsels(member.getCounsels())
                .email(member.getEmail())
                .memberBelong(member.getMemberBelong())
                .memberType(member.getMemberType())
                .name(member.getName())
                .password(member.getPassword())
                .file(modifyMemberFile)
                .build();
        memberService.save(modifyMember);

        Applicant modifyApplicant = getResultApplicant(Applicant.builder()
                .name(resultApplicant.getName())
                .application(resultApplicant.getApplication())
                .id(resultApplicant.getId())
                .schoolNum(resultApplicant.getSchoolNum())
                .school(resultApplicant.getSchool())
                .isAdminApply(false)
                .schoolYear(resultApplicant.getSchoolYear())
                .isApply(true)
                .member(resultApplicant.getMember())
                .major(resultApplicant.getMajor())
                .isSelect(resultApplicant.isSelect()));

        ResponseDTO response = getResponseDTO(modifyMemberFile, modifyMember, modifyApplicant);
        return ResponseEntity.ok().body(response);
    }
    private ResponseDTO getResponseDTO(List<FileEntity> modifyMemberFile, Member modifyMember, Applicant modifyApplicant) {
        List<FileDTO> fileDTOs = new ArrayList<>();
        for (FileEntity fe : modifyMemberFile) {
            if (fe.getFileType().equals("APPLY")) {
                FileDTO temp = FileDTO.builder()
                        .filePath(fe.getFilePath())
                        .fileType(fe.getFileType())
                        .fileName(fe.getFileName())
                        .fileExtension(fe.getFileExtension())
                        .build();
                fileDTOs.add(temp);
            }
        }

        ClientApplicantDTO clientApplicantDTO = ClientApplicantDTO.builder()
                .email(modifyMember.getEmail())
                .major(modifyApplicant.getMajor())
                .name(modifyApplicant.getName())
                .schoolNumber(modifyApplicant.getSchoolNum())
                .files(fileDTOs)
                .build();
        List<ClientApplicantDTO> res = new ArrayList<>();
        res.add(clientApplicantDTO);
        ResponseDTO response = ResponseDTO.<ClientApplicantDTO>builder().data(res).build();
        return response;
    }

    private Applicant getResultApplicant(Applicant.ApplicantBuilder applyDTO) {
        Applicant applicant = applyDTO
                .build();
        Applicant resultApplicant = applyService.saveApplicant(applicant);
        return resultApplicant;
    }

    @PostMapping("/zip-download")
    public ResponseEntity<?> resourceFileDownload(@AuthenticationPrincipal int id) {
        log.info("ENTER /apply/zip-download - Accessor : "+memberService.findByid(id).getEmail());
        try {
            String filesToZip = fileService.filesToZip();
            Resource resource = resourceLoader.getResource("file:"+filesToZip);
            File file = resource.getFile();

            log.info("LEAVE /apply/zip-download - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                    .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(resource);
        } catch (FileNotFoundException e) {
            log.error("FILE NOT FOUND EXCEPTION /apply/zip-download - Accessor: "+memberService.findByid(id).getEmail());
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            log.error("IO EXCEPTION /apply/zip-download - Accessor: "+memberService.findByid(id).getEmail());
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/download")
    public ResponseEntity<?> fileDownload(@AuthenticationPrincipal int id, @RequestBody ClientApplicantDTO dto) {
        log.info("ENTER /apply/download - Accessor : "+memberService.findByid(id).getEmail());
        if (memberService.findByEmail(dto.getEmail())) {
            Member member = memberService.findByEmailData(dto.getEmail());
            FileEntity file = fileService.findByMemberId(member);
            try {
                Resource resource = resourceLoader.getResource("file:"+file.getFilePath()+file.getFileName());
                File binaryFile = resource.getFile();

                log.info("LEAVE /apply/download - Accessor : "+memberService.findByid(id).getEmail());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + binaryFile.getName() + "\"")
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryFile.length()))
                        .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM.toString())
                        .body(resource);

            } catch (FileNotFoundException e) {
                log.error("FILE NOT FOUND EXCEPTION /apply/download - Accessor: "+memberService.findByid(id).getEmail());
                log.error(e.getMessage());
                return ResponseEntity.badRequest().body(null);
            } catch (IOException e) {
                log.error("FILE NOT FOUND EXCEPTION /apply/download - Accessor: "+memberService.findByid(id).getEmail());
                log.error(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            log.error("ERROR /apply/download - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("Not User");
        }
    }
    @GetMapping("/applicant-read-data")
    public ResponseEntity<?> readApplicant(@AuthenticationPrincipal int id, @RequestParam String year, @RequestParam String month) {
        try {
            log.info("ENTER /apply/applicant-read-data - Accessor: "+memberService.findByid(id).getEmail());
        } catch (NullPointerException ne) {
            ResponseDTO response = ResponseDTO.builder().error("NO USER").build();
            log.info("EXCEPTION /apply/applicant-read-data - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body(response);
        }
        if (memberService.findByid(id).isAdmin()) {
            List<Applicant> list = applyService.getDateApplicant(year+"-"+month);
            List<ApplicantDTO> applicantDTOs = new ArrayList<>();

            for (Applicant applicant : list) {
                List<FileDTO> fileDTOs = new ArrayList<>();
                for (FileEntity file : applicant.getFiles()) {
                    FileDTO fileDTO = FileDTO.builder()
                            .fileExtension(file.getFileExtension())
                            .fileName(file.getFileName())
                            .fileType(file.getFileType())
                            .filePath(file.getFilePath())
                            .build();
                    fileDTOs.add(fileDTO);
                }
                ApplicantDTO dto = ApplicantDTO.builder()
                        .schoolYear(applicant.getSchoolYear())
                        .school(applicant.getSchool())
                        .name(applicant.getName())
                        .application(applicant.getApplication())
                        .major(applicant.getMajor())
                        .schoolNumber(applicant.getSchoolNum())
                        .isSelect(applicant.isSelect())
                        .isApply(applicant.isApply())
                        .email(applicant.getMember().getEmail())
                        .isAdminSelect(applicant.isAdminApply())
                        .files(fileDTOs)
                        .build();
                applicantDTOs.add(dto);
                fileDTOs = new ArrayList<>();
            }
            return ResponseEntity.ok().body(applicantDTOs);
        } else {
            ResponseDTO response = ResponseDTO.builder().error("NO ADMIN").build();
            log.info("NO-ADMIN /apply/applicant-read-data - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body(response);
        }

    }
    @GetMapping("/all")
    public ResponseEntity<?> showAllApplicant(@AuthenticationPrincipal int id) {
        log.info("ENTER /apply/all - Accessor : "+memberService.findByid(id).getEmail());
        Member adminMember = memberService.findByid(id);
        if(adminMember.isAdmin()) {
            List<Applicant> applicants = applyService.findAllApplicant();
            List<ApplicantDTO> applicantDTOs = new ArrayList<>();
            List<FileDTO> fileDTOs = new ArrayList<>();
            for(Applicant temp : applicants) {
                for (FileEntity file : temp.getFiles()) {
                    if(file.getFileType().equals("APPLY")) {
                        FileDTO dto = FileDTO.builder()
                                .fileExtension(file.getFileExtension())
                                .fileName(file.getFileName())
                                .filePath(file.getFilePath())
                                .fileType(file.getFileType())
                                .build();
                        fileDTOs.add(dto);
                    }
                }
                ApplicantDTO dto = ApplicantDTO.builder()
                        .email(temp.getMember().getEmail())
                        .name(temp.getName())
                        .application(temp.getApplication())
                        .isApply(temp.isApply())
                        .isAdminSelect(temp.isAdminApply())
                        .school(temp.getSchool())
                        .isSelect(temp.isSelect())
                        .schoolNumber(temp.getSchoolNum())
                        .major(temp.getMajor())
                        .school(temp.getSchool())
                        .schoolYear(temp.getSchoolYear())
                        .files(fileDTOs)
                        .build();
                applicantDTOs.add(dto);
                fileDTOs = new ArrayList<>();
            }
            ResponseDTO response = ResponseDTO.<ApplicantDTO>builder().data(applicantDTOs).build();
            log.info("LEAVE /apply/all - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(response);
        } else {
            log.error("ERROR /apply/all - Accessor : "+memberService.findByid(id).getEmail());
            ResponseDTO response = ResponseDTO.builder().error("NOT ADMIN").build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/forced-select")
    public ResponseEntity<?> forcedSelect(@AuthenticationPrincipal int id, @RequestBody ApplyForcedSelectRequestDTO dto) {
        try {
            log.info("ENTER /apply/forced-select - Accessor : " + memberService.findByid(id).getEmail());
            Member member = memberService.findByEmailData(dto.getEmail());
            log.info(member.getEmail());
            Applicant applicant = applyService.getByMemberId(member.getId());
            applicant.setSelect(!applicant.isSelect());
            Applicant saveApplicant = applyService.saveApplicant(applicant);

            if (saveApplicant.isSelect()) {
                String year = String.valueOf(LocalDate.now().getYear()%2022+1);
                Trainee trainee = Trainee.builder()
                        .applicant(applicant)
                        .cardinal(year)
                        .build();
                Trainee saveTrainee = traineeService.saveTrainee(trainee);
                saveApplicant.setTrainee(saveTrainee);
            } else {
                Trainee trainee = traineeService.getApplicantId(saveApplicant.getId());
                traineeService.deleteEntity(trainee);
            }
            return ResponseEntity.ok().body("OK");
        } catch (NullPointerException ne) {
            log.error("NULL EXCEPTION /apply/forced-select");
            return ResponseEntity.badRequest().body("NULL EXCEPTION /apply/forced-select");
        } catch (Exception e) {
            log.error("EXCEPTION /apply/forced-select");
            return ResponseEntity.badRequest().body("EXCEPTION /apply/forced-select");
        }
    }
    @PostMapping("/trainee-select")
    public ResponseEntity<?> traineeSelect(@AuthenticationPrincipal int id) {
        log.info("ENTER /apply/trainee-select - Accessor: "+memberService.findByid(id).getEmail());
        List<SaveTraineeResponseDTO> traineeList = new ArrayList<>();
        try {
            Member member = memberService.findByid(id);
            Applicant applicant = applyService.getByMemberId(id);
            applicant.setSelect(true);
            Applicant saveApplicant = applyService.saveApplicant(applicant);

            String year = String.valueOf(LocalDate.now().getYear()%2022+1);
            Trainee trainee = Trainee.builder()
                    .applicant(applicant)
                    .cardinal(year)
                    .build();
            Trainee saveTrainee = traineeService.saveTrainee(trainee);

            Applicant temp = applyService.getByMemberId(id);
            temp.setTrainee(saveTrainee);
            Applicant applicant1 = applyService.saveApplicant(temp);

            log.info(saveTrainee.getCardinal());

            SaveTraineeResponseDTO saveTraineeResponseDTO = SaveTraineeResponseDTO.builder()
                    .cardinal(saveTrainee.getCardinal())
                    .name(saveApplicant.getName())
                    .build();
            traineeList.add(saveTraineeResponseDTO);
            ResponseDTO response = ResponseDTO.<SaveTraineeResponseDTO>builder().data(traineeList).build();
            log.info("LEAVE /apply/trainee-select - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(response);
        } catch (NullPointerException ne) {
            log.error("NULL POINTER EXCEPTION /apply/trainee-select - Accessor: "+memberService.findByid(id).getEmail());
            ResponseDTO response = ResponseDTO.builder().error("NOT USER").build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/select")
    public ResponseEntity<?> selectApplicant(@AuthenticationPrincipal int id, @RequestBody List<SelectApplicantDTO> dto) {
        log.info("ENTER /apply/select - Accessor : "+memberService.findByid(id).getEmail());
        List<SaveTraineeResponseDTO> traineeList = new ArrayList<>();
        List<ApplicantDTO> res = new ArrayList<>();
        log.info(dto.toString());
        for (SelectApplicantDTO temp : dto) {
            log.info(temp.getNameEmail());
            Member member = null;
            try {
                member = memberService.findByEmailData(temp.getNameEmail());
                Applicant applicant = applyService.getByMemberId(member.getId());
                applicant.setAdminApply(!applicant.isAdminApply());
                Applicant modifyApplicant = applyService.saveApplicant(applicant);
                log.info(String.valueOf(modifyApplicant.isSelect()));

                ApplicantDTO result = ApplicantDTO.builder()
                        .email(member.getEmail())
                        .isApply(modifyApplicant.isApply())
                        .isSelect(modifyApplicant.isSelect())
                        .schoolNumber(modifyApplicant.getSchoolNum())
                        .major(modifyApplicant.getMajor())
                        .application(modifyApplicant.getApplication())
                        .name(modifyApplicant.getName())
                        .school(modifyApplicant.getSchool())
                        .build();

                res.add(result);
            } catch (Exception e) {
                log.error("EXCEPTION /apply/select - Accessor : "+memberService.findByid(id).getEmail());
                log.error(e.getMessage());
            }
        }
        ResponseDTO response = ResponseDTO.<ApplicantDTO>builder().data(res).build();
        log.info("LEAVE /apply/select - Accessor : "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search-applicant")
    public ResponseEntity<?> searchApplicant(@AuthenticationPrincipal int id, @RequestBody SearchApplicantRequestDTO dto) {
        log.info("ENTER /apply/search-applicant - Accessor: "+memberService.findByid(id).getEmail());
        if (memberService.findByid(id).isAdmin()) {
            List<ApplicantDTO> applicantDTOs = new ArrayList<>();
            List<FileDTO> fileDTOs = new ArrayList<>();
            List<Applicant> searchApplicant = applyService.getSearchApplicant(dto.getType(), dto.getKeyword());
            if (searchApplicant.isEmpty()) {
                log.info("LEAVE /apply/search-applicant - Accessor: "+memberService.findByid(id).getEmail());
                return ResponseEntity.badRequest().body(
                        ResponseDTO.builder().error("EMPTY APPLICANT").build()
                );
            }
            for (Applicant applicant : searchApplicant) {
                for (FileEntity file : applicant.getFiles()) {
                    fileDTOs.add(FileDTO.entityToDTO(file));
                }
                ApplicantDTO appDTO = ApplicantDTO.entityToDTO(applicant);
                appDTO.setEmail(applicant.getMember().getEmail());
                appDTO.setFiles(fileDTOs);
                applicantDTOs.add(appDTO);
                fileDTOs = new ArrayList<>();
            }
            ResponseDTO response = ResponseDTO.<ApplicantDTO>builder().data(applicantDTOs).build();
            log.info("LEAVE /apply/search-applicant - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(response);
        } else {
            ResponseDTO response = ResponseDTO.builder().error("NO ADMIN").build();
            log.error("PERMISSION ERROR /apply/search-applicant - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/checkApplicant")
    public ResponseEntity<?> checkApplicant(@AuthenticationPrincipal int id) {
        log.info("ENTER /apply/checkApplicant - Accessor: "+memberService.findByid(id).getEmail());
        Boolean checkApplicant = applyService.checkApplicant(memberService.findByid(id));
        if (checkApplicant) {
            List<String> res = new ArrayList<>();
            res.add("exist user");
            ResponseDTO response = ResponseDTO.builder().error("400").build();
            log.info("ERROR /apply/checkApplicant - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(response);
        } else {
            List<String> res = new ArrayList<>();
            res.add("OK");
            ResponseDTO response = ResponseDTO.<String>builder().data(res).build();
            log.info("LEAVE /apply/checkApplicant - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(response);
        }
    }


}
