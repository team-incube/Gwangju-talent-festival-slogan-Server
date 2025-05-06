package team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppendSloganRequest {

    @NotBlank
    private String slogan;

    @NotBlank
    private String description;

    @NotBlank
    private String school;

    @NotNull
    private Integer grade;

    @NotNull
    private Integer classNum;

    @NotBlank
    private String phoneNumber;
}

