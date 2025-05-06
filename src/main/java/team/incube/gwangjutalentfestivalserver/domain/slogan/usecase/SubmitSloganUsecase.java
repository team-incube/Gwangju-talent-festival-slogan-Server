package team.incube.gwangjutalentfestivalserver.domain.slogan.usecase;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request.AppendSloganRequest;
import team.incube.gwangjutalentfestivalserver.domain.slogan.entity.Slogan;
import team.incube.gwangjutalentfestivalserver.domain.slogan.repository.SloganRepository;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.google.adapter.GoogleSheetsAdapter;

@Service
@RequiredArgsConstructor
public class AppendSloganUsecase {
    private final SloganRepository sloganRepository;
    private final GoogleSheetsAdapter googleSheetsAdapter;

    @Transactional
    public void execute(AppendSloganRequest request) {
        // DB 저장
        Slogan slogan = Slogan.builder()
                .slogan(request.getSlogan())
                .description(request.getDescription())
                .school(request.getSchool())
                .grade(request.getGrade())
                .classNum(request.getClassNum())
                .phoneNumber(request.getPhoneNumber())
                .build();

        sloganRepository.save(slogan);

        // Google Sheets 반영
        googleSheetsAdapter.appendSlogan(request);
    }
}
