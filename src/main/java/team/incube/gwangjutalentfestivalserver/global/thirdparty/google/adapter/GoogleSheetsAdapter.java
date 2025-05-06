package team.incube.gwangjutalentfestivalserver.global.thirdparty.google.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request.SubmitSloganRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GoogleSheetsAdapter {
    private final String spreadsheetId;
    private final String spreadsheetPage;
    private final String accountCredential;

    public GoogleSheetsAdapter(
        @Value("${google.sheets.id}")
        String spreadsheetId,
        @Value("${google.sheets.page}")
        String spreadsheetPage,
        @Value("${google.account-credential}")
        String accountCredential
    ){
        this.spreadsheetId = spreadsheetId;
        this.spreadsheetPage = spreadsheetPage;
        this.accountCredential = accountCredential;
    }

    public void appendSlogan(SubmitSloganRequest request) {
        try {
            var transport = GoogleNetHttpTransport.newTrustedTransport();
            var jsonFactory = GsonFactory.getDefaultInstance();

            var credentialsStream = new ByteArrayInputStream(accountCredential.getBytes(StandardCharsets.UTF_8));
            var credential = GoogleCredentials.fromStream(credentialsStream)
                    .createScoped(List.of(SheetsScopes.SPREADSHEETS));

            var sheetsService = new Sheets.Builder(transport, jsonFactory, new HttpCredentialsAdapter(credential))
                    .setApplicationName("Slogan Submission")
                    .build();

            var valueRange = new ValueRange().setValues(List.of(List.of(
                request.getSchool(),
                String.valueOf(request.getGrade()),
                String.valueOf(request.getClassNum()),
                request.getPhoneNumber(),
                request.getSlogan(),
                request.getDescription()
            )));

            sheetsService.spreadsheets().values()
                    .append(spreadsheetId, spreadsheetPage, valueRange)
                    .setValueInputOption("RAW")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

        } catch (Exception e) {
            throw new RuntimeException("Google Sheets 연동 실패: " + e.getMessage(), e);
        }
    }
}
