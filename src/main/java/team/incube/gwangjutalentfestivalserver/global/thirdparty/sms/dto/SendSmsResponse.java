package team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsContentType;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsFile;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsMessage;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsResponse {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
}