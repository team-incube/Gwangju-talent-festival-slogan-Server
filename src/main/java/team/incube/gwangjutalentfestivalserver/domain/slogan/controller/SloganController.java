package team.incube.gwangjutalentfestivalserver.domain.slogan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request.SloganRequest;
import team.incube.gwangjutalentfestivalserver.domain.slogan.service.SloganService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slogan")
public class SloganController {

    private final SloganService sloganService;

    @PostMapping("/submit")
    public ResponseEntity<Void> submit(@Valid @RequestBody SloganRequest request) {
        sloganService.submit(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

