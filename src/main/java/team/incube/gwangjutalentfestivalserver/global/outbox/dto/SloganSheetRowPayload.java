package team.incube.gwangjutalentfestivalserver.global.outbox.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SloganSheetRowPayload {
    private Long sloganId;
    private String slogan;
    private String description;
    private String school;
    private Integer grade;
    private Integer classroom;
    private String name;
    private String phoneNumber;
}
