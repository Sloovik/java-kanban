package main;

import main.manager.FileBackedTasksManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import static main.task.StatusEnum.NEW;

/**
 * ��������� ������ ������ �� �������� ���.
 */

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault("data/data.csv");

        Integer task1 = taskManager.addTask(new Task(1, "������ ����", "��������", NEW));
        Integer task2 = taskManager.addTask(new Task(2, "������ ����", "������ �� �����", NEW));

        Integer epic1 = taskManager.addEpic(new Epic(3, "��� ������ ����", "��������� � �����"));

        Integer subtask1 = taskManager.addSubtask(
                new Subtask("������ ������� ������� �����", "������� ����", NEW, 3));

        Integer subtask2 = taskManager.addSubtask(
                new Subtask("������ ������� ������� �����", "��������", NEW, epic1));

        Integer epic2 = taskManager.addEpic(new Epic(6, "������ ����", "��������� � �������"));

        Integer subtask3 = taskManager.addSubtask(
                new Subtask("������ ������� ������� �����", "���������", NEW, epic2));

        taskManager.getTaskByID(task1);
        taskManager.getTaskByID(task2);
        taskManager.getEpicByID(epic1);
        taskManager.getEpicByID(epic2);
        taskManager.getSubTaskByID(subtask1);
        taskManager.getSubTaskByID(subtask2);
        taskManager.getSubTaskByID(subtask3);
        taskManager.getHistory().forEach(s -> System.out.println(s.getId()));

        System.out.println("-----");
        System.out.println(taskManager.getSubtaskList());
        taskManager.removeEpic(epic1);
        taskManager.getHistory().forEach(s -> System.out.println(s.getId()));

        System.out.println("-----");

        System.out.println(taskManager.getSubtaskList());
        taskManager.removeEpic(epic2);
        System.out.println(taskManager.getSubtaskList());
        taskManager.getHistory().forEach(s -> System.out.println(s.getId()));

        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile("data/data.csv");

        if (!fileBackedTasksManager.getHistory().equals(taskManager.getHistory())) {
            System.out.println("Histories not equal");
            throw new IllegalArgumentException("Histories not equal");
        } else {
            System.out.println("Histories are equal! <3");
        }
    }

}
