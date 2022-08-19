package com.cognizant.sheetsDemo.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleServiceAuthentication implements InitializingBean {

    private final String APP_NAME;
    private final JsonFactory JSON_FACTORY;
    private final List<String> SCOPES;
    private final String CREDENTIALS_FILE_PATH;
    private final String TOKENS_DIRECTORY_PATH;
    private final String REDIRECT_URL;
    private final String ACCESS_TYPE;

    private GoogleAuthorizationCodeFlow authFlow;

    public GoogleServiceAuthentication(@Value("${app.name}") String APP_NAME,
                                       @Value("${app.access-type}") String ACCESS_TYPE,
                                       @Value("${app.redirect-url}") String REDIRECT_URL,
                                       @Value("${app.credential-path}") String CREDENTIALS_FILE_PATH,
                                       @Value("${app.tokens.file-storage-path}") String TOKENS_DIRECTORY_PATH){
        this.APP_NAME = APP_NAME;
        this.JSON_FACTORY = GsonFactory.getDefaultInstance();
        this.SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        this.REDIRECT_URL = REDIRECT_URL;
        this.CREDENTIALS_FILE_PATH = CREDENTIALS_FILE_PATH;
        this.TOKENS_DIRECTORY_PATH = TOKENS_DIRECTORY_PATH;
        this.ACCESS_TYPE = ACCESS_TYPE;
    }

    public GoogleAuthorizationCodeFlow getAuthFlow() {
        return authFlow;
    }

    private Credential generateCredential(String token){
        try {
            GoogleAuthorizationCodeTokenRequest tokenRequest = this.authFlow.newTokenRequest(token);
            GoogleTokenResponse tokenResponse = tokenRequest.setRedirectUri(REDIRECT_URL).execute();
            return this.authFlow.createAndStoreCredential(tokenResponse, "user");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Sheets getSheetsService(String token) throws GeneralSecurityException, IOException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(
                httpTransport,
                JSON_FACTORY,
                this.generateCredential(token)
        )
                .setApplicationName(this.APP_NAME)
                .build();
    }
    public GoogleAuthorizationCodeRequestUrl getNewAuthorizationUrl(){
        GoogleAuthorizationCodeRequestUrl url = this.authFlow.newAuthorizationUrl();
        url.setRedirectUri(REDIRECT_URL);
        return url;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // Load data about this app to authorise in google API
        File file = ResourceUtils.getFile("classpath:"+CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(new FileInputStream(file))
        );
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                // Scopes defining what permission user have to provide us to work with user's data.
                SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                // The offline access type enables usage of refresh token.
                // https://developers.google.com/identity/protocols/oauth2/web-server#identify-access-scopes
                .setAccessType(ACCESS_TYPE)
                .build();
    }
}
