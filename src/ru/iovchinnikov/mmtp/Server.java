package ru.iovchinnikov.mmtp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

// TODO При добавлении комментария к таску: Пользователь <имя пользователя>  оставил в задаче <ссылка>  новый комментарий <текст комментария>.
// TODO При изменении статуса таска: Пользователь <имя пользователя> изменил статус в задаче <ссылка>  со  <статус> на <статус>.
// TODO При назначении таска на пользователя: Пользователь <имя пользователя> назначил <имя пользователя> на задачу <ссылка>
// TODO Добавление/удаление файла к задаче: Пользователь <имя пользователя> добавил/удалил в задаче <ссылка> файл <название файла>
// TODO Изменение Remaining effort: Пользователь <Имя пользователя> в задаче <ссылка на задачу> изменил Remaining effort <>
// TODO Пользователь <имя пользователя> изменил в задаче <ссылка> статус на <статус>.
// TODO Прикреплен документ <ссылка>.
// TODO Добавлен комментарий: "<текст>".
public class Server {

    private static Settings appSettings;

    static class HandlerImpl implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String mattermostEndpoint = appSettings.readStringSetting("host");
            String tuleapEncoding = appSettings.readStringSetting("encoding");

            InputStream is = httpExchange.getRequestBody();
            InputStreamReader isr = new InputStreamReader(is, tuleapEncoding);
            BufferedReader rq = new BufferedReader(isr);
            String str = URLDecoder.decode(rq.readLine(), tuleapEncoding);
            try {
                sendJSON(mattermostEndpoint, generateAnswer(str.substring(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateAnswer(String str) throws ParseException {
        JSONObject out = new JSONObject();
        JSONObject json = (JSONObject) new JSONParser().parse(str);
        String name = appSettings.readStringSetting("botName");

        String result = new PropertyBuilder(json)
                .newLine().appendUser()
                .appendAction().appendArtifactRef()
                .newLine().appendDetailsIfChanged()
                .newLine().appendCommentIfExists()
                .getResult();

        out.put("username", name);
        out.put("text", result);
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
        appSettings = new Settings("properties.json");
        int port = appSettings.readLongSetting("port").intValue();
        String endpoint = appSettings.readStringSetting("endpoint");

        try {
            HttpServer srv = HttpServer.create(new InetSocketAddress(port), 0);
            srv.createContext(endpoint, new HandlerImpl());
            srv.setExecutor(null);
            srv.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
