package team.incube.gwangjutalentfestivalserver.domain.slogan.usecase;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request.SubmitSloganRequest;
import team.incube.gwangjutalentfestivalserver.domain.slogan.entity.Slogan;
import team.incube.gwangjutalentfestivalserver.domain.slogan.enums.SheetType;
import team.incube.gwangjutalentfestivalserver.domain.slogan.repository.SloganRepository;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.google.adapter.GoogleSheetsAdapter;

@Service
@RequiredArgsConstructor
public class SubmitSloganUsecase {
    private final SloganRepository sloganRepository;
    private final GoogleSheetsAdapter googleSheetsAdapter;

    @Transactional
    public void execute(SubmitSloganRequest request) {
        Slogan slogan = Slogan.builder()
            .slogan(request.getSlogan())
            .description(request.getDescription())
            .school(request.getSchool())
            .grade(request.getGrade())
            .classroom(request.getClassroom())
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .build();
        sloganRepository.save(slogan);

        googleSheetsAdapter.appendSlogan(request, SheetType.PUBLIC);
        googleSheetsAdapter.appendSlogan(request, SheetType.PRIVATE);
    }
}
