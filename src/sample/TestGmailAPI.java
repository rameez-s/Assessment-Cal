package sample;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestGmailAPI{
    private static String client_secret = "jXy0O9yNMaIskpIpqS9STwuw";
    private static String client_ID = "733309529446-6ic410d707p3ocl3gh2v06uqe83h1psi.apps.googleusercontent.com";

    private static final String NAME_OF_APPLICATION =
            "Assessment Calendar";

    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    private static HttpTransport HTTP_TRANSPORT;

    private static final List<String> SCOPES =
            Arrays.asList(GmailScopes.GMAIL_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public static Credential authorize() throws IOException {
        //authorize the user
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, client_ID, client_secret, SCOPES)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    public static Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        //return a Gmailservice
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(NAME_OF_APPLICATION)
                .build();
    }

    public static void main(String[] args) throws IOException {
        Gmail service = getGmailService();

        //Print the email of the user
        String user = "me";
        System.out.println(service.users().getProfile(user).execute().getEmailAddress());

    }
}