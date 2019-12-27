package ru.iovchinnikov.mmtp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
// TODO При создании нового таска: Пользователь <имя пользователя>  создал новую задачу <ссылка>.
// TODO При добавлении комментария к таску: Пользователь <имя пользователя>  оставил в задаче <ссылка>  новый комментарий <текст комментария>.
// TODO При изменении статуса таска: Пользователь <имя пользователя> изменил статус в задаче <ссылка>  со  <статус> на <статус>.
// TODO При назначении таска на пользователя: Пользователь <имя пользователя> назначил <имя пользователя> на задачу <ссылка>
// TODO При изменении описания задачи: Пользователь <имя пользователя> изменил в задаче <ссылка> описание <новое описание задачи>
// TODO Добавление/удаление файла к задаче: Пользователь <имя пользователя> добавил/удалил в задаче <ссылка> файл <название файла>
// TODO Изменение Remaining effort: Пользователь <Имя пользователя> в задаче <ссылка на задачу> изменил Remaining effort <>
// TODO Пользователь <имя пользователя> изменил в задаче <ссылка> статус на <статус>.
// TODO Прикреплен документ <ссылка>.
// TODO Добавлен комментарий: "<текст>".
public class Server {

    private static final String HOST = "http://1.0.0.137:8088/hooks/et9og89o93f9truwybccumyrkc"; // test channel endpoint
//    private static final String HOST = "http://1.0.0.137:8088/hooks/ojxdgbsxajbcigxs1b3abpzdmh";
    private static final int PORT = 12345;
    private static final String ENDPOINT = "/post";
//    private static final String ENDPOINT = "/cmec";



    static class HandlerImpl implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            InputStream is = httpExchange.getRequestBody();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader rq = new BufferedReader(isr);
            String str = URLDecoder.decode(rq.readLine(), "UTF-8");

            try {
                sendJSON(HOST, generateAnswer(str.substring(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateAnswer(String str) throws ParseException {
        JSONObject out = new JSONObject();
        JSONObject json = (JSONObject) new JSONParser().parse(str);

        String result = new PropertyBuilder(json)
                .newLine().appendUser()
                .appendAction().appendCustom("d ")
                .appendArtifactId()
                .getResult();

        out.put("username", "Tuleap");
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

        try {
            HttpServer srv = HttpServer.create(new InetSocketAddress(PORT), 0);
            srv.createContext(ENDPOINT, new HandlerImpl());
            srv.setExecutor(null);
            srv.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
