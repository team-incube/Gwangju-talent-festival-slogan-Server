package team.incube.gwangjutalentfestivalserver.domain.slogan.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request.SloganRequest;
import team.incube.gwangjutalentfestivalserver.domain.slogan.entity.Slogan;
import team.incube.gwangjutalentfestivalserver.domain.slogan.repository.SloganRepository;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.google.adapter.GoogleSheetsService;

@Service
@RequiredArgsConstructor
public class SloganService {

    private final SloganRepository sloganRepository;
    private final GoogleSheetsService googleSheetsService;

    public void submit(SloganRequest request) {
        // DB 저장
        Slogan slogan = Slogan.builder()
                .slogan(request.getSlogan())
                .description(request.getDescription())
                .school(request.getSchool())
                .grade(request.getGrade())
                .classNum(request.getClassNum())
                .phone(request.getPhone())
                .build();

        sloganRepository.save(slogan);

        // Google Sheets 반영
        googleSheetsService.appendSlogan(request);
    }
}
