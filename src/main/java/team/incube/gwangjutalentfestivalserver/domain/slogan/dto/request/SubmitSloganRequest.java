package team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSloganRequest {
    @NotBlank
    private String slogan;

    @NotBlank
    private String description;

    @NotBlank
    private String school;

    @NotBlank
    private String name;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer grade;

    @NotNull
    private Integer classroom;

    @Pattern(regexp = "^010\\d{8}$")
    private String phoneNumber;
}

