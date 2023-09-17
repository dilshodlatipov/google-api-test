package uz.pdp.googleapitest.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
public class SpreadsheetConfig {
    //        @Value("file.path.credentials")
    private final String CREDENTIALS_FILE_PATH = "C:\\Users\\HP\\IdeaProjects\\google-api-test\\src\\main\\resources\\web_app.json";
    //        @Value("file.path.tokens")
    private final String TOKENS_DIRECTORY_PATH = "tokens";
    //    @Value("spreadsheets.port")
    private final Integer PORT = 8888;
    //    @Value("spreadsheets.application.name")
    private final String APPLICATION_NAME = "Google Sheet API Integration";

    @Bean
    public JsonFactory jsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    public NetHttpTransport netHttpTransport() {
        try {
            return GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public List<String> scopes() {
        return List.of(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
    }

    @Bean
    public Credential credential() {
        try (InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(jsonFactory(), new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    netHttpTransport(), jsonFactory(), clientSecrets, scopes()
            )
                    .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                    .setPort(PORT).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Sheets sheets() {
        return new Sheets.Builder(netHttpTransport(), jsonFactory(), credential())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}
