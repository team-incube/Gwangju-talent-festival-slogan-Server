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
public class SubmitSloganRequest {
    @NotBlank
    private String slogan;

    @NotBlank
    private String description;

    @NotBlank
    private String school;

    @NotBlank
    private String name;

    @NotNull
    @Size(min = 1, max = 6)
    private Integer grade;

    @NotNull
    private Integer classNum;

    @Pattern(regexp = "^010\\d{8}$")
    private String phoneNumber;
}

