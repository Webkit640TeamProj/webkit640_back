package com.example.webkit640.controller;


import com.example.webkit640.dto.request.CalendarDeleteRequestDTO;
import com.example.webkit640.dto.request.CalendarInsertRequestDTO;
import com.example.webkit640.entity.Calendar;
import com.example.webkit640.service.CalendarService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar")
@Slf4j
@CrossOrigin(origins = "*")
public class CalendarController {
    private final CalendarService calendarService;
    private final MemberService memberService;


    @Autowired
    public CalendarController(CalendarService calendarService, MemberService memberService) {
        this.calendarService = calendarService;
        this.memberService = memberService;
    }

    @GetMapping("/show")
    public ResponseEntity<?> showEvents(@AuthenticationPrincipal int id) {
        log.info("ENTER /calendar/show - Accessor: "+memberService.findByid(id).getEmail());
        List<Calendar> calendar = calendarService.getCalendar();
        log.info("LEAVE /calendar/show - Accessor: "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(calendar);
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertEvents(@AuthenticationPrincipal int id, @RequestBody CalendarInsertRequestDTO dto) {
        log.info("ENTER /calendar/insert - Accessor: "+memberService.findByid(id).getEmail());
        calendarService.saveEvent(Calendar.builder()
                        .title(dto.getTitle())
                        .end(dto.getEnd())
                        .start(dto.getStart())
                .build());
        log.info("LEAVE /calendar/insert - Accessor: "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body("OK");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEvents(@AuthenticationPrincipal int id, @RequestBody CalendarDeleteRequestDTO dto) {
        log.info("ENTER /calendar/delete - Accessor: "+memberService.findByid(id).getEmail());
        calendarService.deleteCalendarEvent(dto.getTitle(), dto.getStart(), dto.getEnd());
        log.info("LEAVE /calendar/delete - Accessor: "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body("OK");
    }
}
