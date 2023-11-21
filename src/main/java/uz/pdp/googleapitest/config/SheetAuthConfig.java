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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SheetAuthConfig {
    @Value("${spreadsheets.file.path.tokens}")
    private String tokensDirectoryPath;
    @Value("${spreadsheets.port}")
    private Integer port;
    @Value("${spreadsheets.application.name}")
    private String applicationName;
    private final String CREDENTIALS_FILE_PATH = "sheet_credentials.json";
    private static final List<String> scopes = new ArrayList<>() {{
        add(SheetsScopes.SPREADSHEETS);
        add(SheetsScopes.DRIVE);
    }};

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
    public Credential credential() {
        try (InputStream in = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream()) {
            if (in == null)
                throw new FileNotFoundException("Resource not found");
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(jsonFactory(), new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    netHttpTransport(), jsonFactory(), clientSecrets, scopes
            ).setDataStoreFactory(new FileDataStoreFactory(new File(tokensDirectoryPath)))
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(port).build();

            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Sheets sheets() {
        return new Sheets.Builder(netHttpTransport(), jsonFactory(), credential())
                .setApplicationName(applicationName)
                .build();
    }
}
