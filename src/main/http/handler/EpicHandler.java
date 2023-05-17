package main.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.adapter.InstantAdapter;
import main.manager.TaskManager;
import main.task.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class EpicHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int statusCode;
        String response;

        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                String query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    statusCode = 200;
                    String jsonString = gson.toJson(taskManager.getEpicList());
                    System.out.println("GET EPICS: " + jsonString);
                    response = gson.toJson(jsonString);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        Epic epic = taskManager.getEpicByID(id);
                        if (epic != null) {
                            response = gson.toJson(epic);
                        } else {
                            response = "���� � ������ id �� ������";
                        }
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "� ������� ����������� ����������� �������� id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "�������� ������ id";
                    }
                }
                break;
            case "POST":
                String bodyRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Epic epic = gson.fromJson(bodyRequest, Epic.class);
                    int id = epic.getId();
                    if (taskManager.getEpicByID(id) != null) {
                        taskManager.updateTask(epic);
                        statusCode = 200;
                        response = "���� � id=" + id + " ��������";
                    } else {
                        System.out.println("CREATED");
                        Epic epicCreated = taskManager.addEpic(epic);
                        System.out.println("CREATED EPIC: " + epicCreated);
                        int idCreated = epicCreated.getId();
                        statusCode = 201;
                        response = "������ ���� � id=" + idCreated;
                    }
                } catch (JsonSyntaxException e) {
                    statusCode = 400;
                    response = "�������� ������ �������";
                }
                break;
            case "DELETE":
                response = "";
                query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.removeAllEpics();
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        taskManager.removeEpic(id); // maybe probs / if probs -> change to remove(id)
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "� ������� ����������� ����������� �������� id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "�������� ������ id";
                    }
                }
                break;
            default:
                statusCode = 400;
                response = "������������ ������";
        }

        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        exchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}