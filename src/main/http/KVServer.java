package main.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * �������: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    private void load(HttpExchange httpExchange) {
        // TODO �������� ��������� �������� �� �����
        try (httpExchange) {
            System.out.println("\n/load");
            if (!hasAuth(httpExchange)) {
                System.out.println("������ �� �����������, ����� �������� � query API_TOKEN �� ��������� API-�����");
                httpExchange.sendResponseHeaders(403, 0);
                return;
            }
            if ("GET".equals(httpExchange.getRequestMethod())) {
                String key = httpExchange.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("Key ��� ���������� ������. Key ����������� � ����: /load/{key}");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                if (data.get(key) == null) {
                    System.out.println("�� ���� ������� ������ ��� ����� '" + key + "', ������ �����������");
                    httpExchange.sendResponseHeaders(404, 0);
                    return;
                }
                String response = data.get(key);
                sendText(httpExchange, response);
                System.out.println("�������� ��� ����� " + key + " ������� ���������� � ����� �� ������!");
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/load ���� GET-������, � �������: " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(HttpExchange httpExchange) throws IOException {
        try (httpExchange) {
            System.out.println("\n/save");
            if (!hasAuth(httpExchange)) {
                System.out.println("������ �������������, ����� �������� � query API_TOKEN �� ��������� ���-�����");
                httpExchange.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(httpExchange.getRequestMethod())) {
                String key = httpExchange.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key ��� ���������� ������. key ����������� � ����: /save/{key}");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(httpExchange);
                if (value.isEmpty()) {
                    System.out.println("Value ��� ���������� ������. value ����������� � ���� �������");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("�������� ��� ����� " + key + " ������� ���������!");
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ��� POST-������, � �������: " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
    }

    private void register(HttpExchange httpExchange) throws IOException {
        try (httpExchange) {
            System.out.println("\n/register");
            if ("GET".equals(httpExchange.getRequestMethod())) {
                sendText(httpExchange, apiToken);
            } else {
                System.out.println("/register ��� GET-������, � ������� " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
    }

    public void start() {
        System.out.println("��������� ������ �� ����� " + PORT);
        System.out.println("������ � �������� http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("�� " + PORT + " ����� ������ ����������!");
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange httpExchange) {
        String rawQuery = httpExchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
    }
}