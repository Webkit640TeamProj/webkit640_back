package com.example.webkit640.controller;

import com.example.webkit640.dto.*;
import com.example.webkit640.dto.request.SelectApplicantDTO;
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

    @PostMapping("/applies")
    public ResponseEntity<?> uploadTest(@RequestPart MultipartFile file, @RequestPart ApplyDTO applyDTO, @AuthenticationPrincipal int id) throws IOException {
        log.info(applyDTO.toString());
        Member member = memberService.findByid(id);
        log.info(member.toString());

        Applicant resultApplicant = getResultApplicant(Applicant.builder()
                .name(applyDTO.getName())
                .application(applyDTO.getApplication())
                .isApply(false)
                .member(member)
                .isSelect(false)
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
        try {
            String filesToZip = fileService.filesToZip();
            Resource resource = resourceLoader.getResource("file:"+filesToZip);
            File file = resource.getFile();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                    .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/download")
    public ResponseEntity<?> fileDownload(@AuthenticationPrincipal int id, @RequestBody ClientApplicantDTO dto) {
        Member member = memberService.findByEmailData(dto.getEmail());
        FileEntity file = fileService.findByMemberId(member);
        try {
            Resource resource = resourceLoader.getResource("file:"+file.getFilePath()+file.getFileName());
            File binaryFile = resource.getFile();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + binaryFile.getName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryFile.length()))
                    .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> showAllApplicant(@AuthenticationPrincipal int id) {
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
                        .name(temp.getName())
                        .application(temp.getApplication())
                        .isApply(temp.isApply())
                        .isSelect(temp.isSelect())
                        .schoolNumber(temp.getSchoolNum())
                        .major(temp.getMajor())
                        .school(temp.getSchool())
                        .files(fileDTOs)
                        .build();
                applicantDTOs.add(dto);
                fileDTOs = new ArrayList<>();
            }
            ResponseDTO response = ResponseDTO.<ApplicantDTO>builder().data(applicantDTOs).build();
            return ResponseEntity.ok().body(response);
        } else {
            ResponseDTO response = ResponseDTO.builder().error("NOT ADMIN").build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/select")
    public ResponseEntity<?> selectApplicant(@AuthenticationPrincipal int id, @RequestBody List<SelectApplicantDTO> dto) {
        log.info(dto.toString());
        List<SaveTraineeResponseDTO> traineeList = new ArrayList<>();
        for (SelectApplicantDTO temp : dto) {
            String[] slicingData = temp.getNameEmail().split(" ");
            log.info(slicingData[1]);
            Member member = null;
            try {
                member = memberService.findByEmailData(slicingData[1]);
                Applicant applicant = applyService.getByMemberId(member.getId());
                applicant.setSelect(true);
                Applicant modifyApplicant = applyService.saveApplicant(applicant);
                log.info(String.valueOf(modifyApplicant.isSelect()));


                LocalDate ld = LocalDate.now();
                String year = String.valueOf(ld.getYear()%2022+1);
                Trainee trainee = Trainee.builder()
                        .applicant(modifyApplicant)
                        .cardinal(year)
                        .build();
                Trainee saveTrainee = traineeService.saveTrainee(trainee);
                log.info(saveTrainee.getCardinal());

                SaveTraineeResponseDTO saveTraineeResponseDTO = SaveTraineeResponseDTO.builder()
                        .cardinal(saveTrainee.getCardinal())
                        .name(modifyApplicant.getName())
                        .build();
                traineeList.add(saveTraineeResponseDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ResponseDTO response = ResponseDTO.<SaveTraineeResponseDTO>builder().data(traineeList).build();
        return ResponseEntity.ok().body(response);
    }
}
