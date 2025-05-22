package team.incube.gwangjutalentfestivalserver.global.thirdparty.google.adapter;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
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
import team.incube.gwangjutalentfestivalserver.domain.slogan.enums.SheetType;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GoogleSheetsAdapter {
    private final String privateSheetId;
    private final String privateSheetPage;
    private final String publicSheetId;
    private final String publicSheetPage;
    private final String accountCredential;

    public GoogleSheetsAdapter(
        @Value("${google.sheets.private-sheet-id}")
        String privateSheetId,
        @Value("${google.sheets.private-sheet-page}")
        String privateSheetPage,
        @Value("${google.sheets.public-sheet-id}")
        String publicSheetId,
        @Value("${google.sheets.public-sheet-page}")
        String publicSheetPage,
        @Value("${google.account-credential}")
        String accountCredential
    ){
        this.privateSheetId = privateSheetId;
        this.privateSheetPage = privateSheetPage;
        this.publicSheetId = publicSheetId;
        this.publicSheetPage = publicSheetPage;
        this.accountCredential = accountCredential;
    }

    public void appendSlogan(SubmitSloganRequest request, SheetType sheetType) {
        String sheetId = sheetType == SheetType.PUBLIC ? publicSheetId : privateSheetId;
        String sheetPage = sheetType == SheetType.PUBLIC ? publicSheetPage : privateSheetPage;

        try {
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(accountCredential.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials credential = GoogleCredentials.fromStream(credentialsStream)
                    .createScoped(List.of(SheetsScopes.SPREADSHEETS));

            Sheets sheetsService = new Sheets.Builder(transport, jsonFactory, new HttpCredentialsAdapter(credential))
                    .setApplicationName("Slogan Submission")
                    .build();

            ValueRange valueRange = new ValueRange().setValues(
                getLists(request, sheetType)
            );

            sheetsService.spreadsheets().values()
                .append(sheetId, sheetPage, valueRange)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
        } catch (Exception e) {
            throw new RuntimeException("Google Sheets 연동 실패: " + e.getMessage(), e);
        }
    }

    private static List<List<Object>> getLists(SubmitSloganRequest request, SheetType sheetType) {
        List<Object> sloganAsList = sheetType == SheetType.PUBLIC ?
            List.of(
                request.getSlogan(),
                request.getDescription()
            ) :
            List.of(
                request.getSchool(),
                String.valueOf(request.getGrade()),
                String.valueOf(request.getClassroom()),
                request.getPhoneNumber(),
                request.getSlogan(),
                request.getDescription()
            );

        return List.of(sloganAsList);
    }
}
