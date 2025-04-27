package team.incube.gwangjutalentfestivalserver.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.request.SendVerifyCodeRequest;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.VerifyCode;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.VerifyCount;
import team.incube.gwangjutalentfestivalserver.domain.auth.repository.VerifyCodeRepository;
import team.incube.gwangjutalentfestivalserver.domain.auth.repository.VerifyCountRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.repository.UserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.adapter.SmsAdapter;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.dto.SendSmsRequest;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsContentType;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.model.SmsType;
import team.incube.gwangjutalentfestivalserver.global.util.RandomUtil;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SendVerifyCodeUsecase {
	private final UserRepository userRepository;
	private final VerifyCodeRepository verifyCodeRepository;
	private final VerifyCountRepository verifyCountRepository;
	private final SmsAdapter smsAdapter;
	private final RandomUtil randomUtil;

	@Transactional
	public void execute(SendVerifyCodeRequest request) {
		String phoneNumber = request.getPhoneNumber();

		if (userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "이미 등록된 전화번호입니다.");
		}

		VerifyCount verifyCount = verifyCountRepository.findByPhoneNumber(phoneNumber)
			.orElseGet(() -> verifyCountRepository.save(
				VerifyCount.builder()
					.phoneNumber(phoneNumber)
					.count(0)
					.build()
			));

		if(verifyCount.getCount() >= 5){
			throw new HttpException(HttpStatus.BAD_REQUEST, "인증 횟수를 초과했습니다.");
		}

		String code = randomUtil.generateRandomCode(6);
		VerifyCode verifyCodeEntity = VerifyCode.builder()
			.phoneNumber(request.getPhoneNumber())
			.expires(Duration.ofMinutes(30).toMillis())
			.code(code)
			.build();

		verifyCodeRepository.save(verifyCodeEntity);

//		SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
//				.files(List.of())
//				.contentType(SmsContentType.COMM)
//				.type(SmsType.SMS)
//				.build();
//		// TODO 수정해야함
//
//		smsAdapter.sendSms(sendSmsRequest);

		System.out.println("인증번호 전송됨");
		System.out.println(code);

		verifyCount.incrementCount();
	}
}
