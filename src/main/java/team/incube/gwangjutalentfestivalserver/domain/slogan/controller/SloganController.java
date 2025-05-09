package team.incube.gwangjutalentfestivalserver.domain.slogan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request.SubmitSloganRequest;
import team.incube.gwangjutalentfestivalserver.domain.slogan.usecase.SubmitSloganUsecase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slogan")
public class SloganController {

    private final SubmitSloganUsecase submitSloganUsecase;

    @PostMapping
    public ResponseEntity<Void> submitSlogan(@Valid @RequestBody SubmitSloganRequest request) {
        submitSloganUsecase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

