package main.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.adapter.InstantAdapter;
import main.manager.TaskManager;
import main.task.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class SubtaskHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                    response = gson.toJson(taskManager.getSubtaskList());
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        Subtask subtask = taskManager.getSubTaskByID(id);
                        if (subtask != null) {
                            response = gson.toJson(subtask);
                        } else {
                            response = "��������� � ������ id �� �������";
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
                    Subtask subtask = gson.fromJson(bodyRequest, Subtask.class);
                    int id = subtask.getId();
                    if (taskManager.getSubTaskByID(id) != null) {
                        taskManager.updateTask(subtask);
                        statusCode = 200;
                        response = "��������� � id=" + id + " ���������";
                    }
                    else {
                        System.out.println("CREATED");
                        Subtask subtaskCreated = taskManager.addSubtask(subtask);
                        System.out.println("CREATED SUBTASK: " + subtaskCreated);
                        int idCreated = subtaskCreated.getId();
                        statusCode = 201;
                        response = "������� ��������� � id=" + idCreated;
                    }
                } catch (JsonSyntaxException e) {
                    response = "�������� ������ �������";
                    statusCode = 400;
                }
                break;
            case "DELETE":
                response = "";
                query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.removeAllSubtasks(); // maybe probs with prioritized tasks or smth like that
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        taskManager.removeSubtask(id);
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