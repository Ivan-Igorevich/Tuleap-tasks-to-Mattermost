package ru.iovchinnikov.mmtp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.internal.ws.addressing.EPRSDDocumentFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Server {
    private static final String ACTION = "action";
    private static final String CHANGES_BY = "submitted_by_details";
    private static final String CHANGES_TS = "submitted_on";
    private static final String LAST_COMMENT = "last_comment";
    private static final String ARTIFACT_VALUES = "values"; //(JSONArray) currentArtifact.get(ARTIFACT_VALUES);

    private static final int TITLE = 0;             // (String) ((Map) values.get(TITLE)).get("value");
    private static final int TYPE = 1;              // (String) (((Map) ((JSONArray) ((Map) values.get(TYPE)).get("values")).get(0)).get("label"));
    private static final int ASSIGNED_TO = 2;       // (String) (((Map) ((JSONArray) ((Map) values.get(ASSIGNED_TO)).get("values")).get(0)).get("display_name"));
    private static final int STATUS = 3;            // (String) (((Map) ((JSONArray) ((Map) values.get(STATUS)).get("values")).get(0)).get("label"));
    private static final int LINKS = 4;
    private static final int ARTIFACT_ID = 5;       // (String) ((Map) values.get(ARTIFACT_ID)).get("value");
    private static final int LAST_UPDATE_TS = 6;    // (String) ((Map) values.get(LAST_UPDATE_TS)).get("value");
    private static final int CROSS_REFS = 7;
    private static final int SUBMITTED_TS = 8;
    private static final int REMAINING_EFFORT = 9;
    private static final int LAST_UPDATE_BY = 10;
    private static final int DETAILS = 11;          // (String) ((Map) values.get(DETAILS)).get("value");
    private static final int SUBMITTED_BY = 12;

    static class HandlerImpl implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            InputStream is = httpExchange.getRequestBody();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader rq = new BufferedReader(isr);
            String str = URLDecoder.decode(rq.readLine(), "UTF-8");
//            System.out.println(str);

            try {
                sendJSON("http://1.0.0.137:8088/hooks/et9og89o93f9truwybccumyrkc", generateAnswer(str.substring(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateAnswer(String str) throws ParseException {
        JSONObject out = new JSONObject();
        StringBuilder answer = new StringBuilder("\"Tuleap artifact update: \n");
        JSONObject json = (JSONObject) new JSONParser().parse(str);
        JSONObject currentArtifact = (JSONObject) json.get("current");
        // submitted by
        answer.append("User ").append(((Map) currentArtifact.get("submitted_by_details")).get("display_name"));
        // main values of the current artifact
        JSONArray values = (JSONArray) currentArtifact.get(ARTIFACT_VALUES);

        String taskTitle = (String) ((Map) values.get(TITLE)).get("value");
        answer.append("\n").append("Current artifact title: ").append(taskTitle);
        String taskType = (String) (((Map) ((JSONArray) ((Map) values.get(TYPE)).get("values")).get(0)).get("label"));
        answer.append("\n").append("Of type: ").append(taskType);

        answer.append("\n").append("Left a comment: ").append((String) ((Map) currentArtifact.get("last_comment")).get("body"));

        answer.append("\"");
        out.put("text", answer);
        System.out.println(out.toString());
        return out.toString();
    }

    private static void sendJSON(String hookURL, String json) throws IOException {
        HttpURLConnection http = (HttpURLConnection) new URL(hookURL).openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        byte[] out = json.getBytes(StandardCharsets.UTF_8);
        int len = out.length;
        http.setFixedLengthStreamingMode(len);
        http.setRequestProperty("Content-type", "application/json; charset=UTF-8");
        http.connect();

        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        try {
            HttpServer srv = HttpServer.create(new InetSocketAddress(12345), 0);
            srv.createContext("/post", new HandlerImpl());
            srv.setExecutor(null);
            srv.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
