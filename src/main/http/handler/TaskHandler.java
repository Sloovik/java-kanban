package main.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.adapter.InstantAdapter;
import main.manager.TaskManager;
import main.task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class TaskHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int statusCode;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("�������������� ������ " + path + " � ������� " + method);

        switch (method) {
            case "GET":
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    statusCode = 200;
                    String jsonString = gson.toJson(taskManager.getTaskList());
                    System.out.println("GET TASKS: " + jsonString);
                    response = gson.toJson(jsonString);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        Task task = taskManager.getTaskByID(id);
                        if (task != null) {
                            response = gson.toJson(task);
                        } else {
                            response = "������ � ������ id �� �������";
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
                String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Task task = gson.fromJson(bodyRequest, Task.class);
                    int id = task.getId();
                    if (taskManager.getTaskByID(id) != null) {
                        taskManager.updateTask(task);
                        statusCode = 201;
                        response = "������ � id=" + id + " ���������";
                    } else {
                        Integer taskCreated = taskManager.addTask(task);
                        System.out.println("CREATED TASK: " + taskCreated);
                        int idCreated = taskCreated;
                        statusCode = 201;
                        response = "������� ������ � id=" + idCreated;
                    }
                } catch (JsonSyntaxException e) {
                    statusCode = 400;
                    response = "�������� ������ �������";
                }
                break;
            case "DELETE":
                response = "";
                query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.removeAllTasks(); // or removeAll
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        taskManager.remove(id);
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

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}