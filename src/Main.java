/**
 * ��������� ������ ������ �� �������� ���.
 */

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Integer task1 = taskManager.add(new Task("������ ����", "��������", StatusEnum.TODO));
        Integer task2 = taskManager.add(new Task("������ ����", "������ �� �����", StatusEnum.TODO));

        Integer epic1 = taskManager.add(new Epic("��� ������ ����", "��������� � �����"));
        Integer subtask1 = taskManager.add(new Subtask("������ ������� ������� �����", "����� �������",
                StatusEnum.TODO, epic1));
        Integer subtask2 = taskManager.add(new Subtask("������ ������� ������� �����", "��������",
                StatusEnum.TODO, epic1));


        Integer epic2 = taskManager.add(new Epic("������ ����", "��������� � �������"));
        Integer subtask3 = taskManager.add(new Subtask("������ ������� ������� �����", "���������",
                StatusEnum.TODO, epic2));

        taskManager.update(new Task(task1, "����� ������ �����", "��������� ���",
                StatusEnum.IN_PROGRESS));
        taskManager.update(new Subtask(subtask1, "����� �������", "������ ���� ��������",
                StatusEnum.DONE, epic1));
        taskManager.update(new Epic(epic2, "����� ������ ����", "������ �����"));

        System.out.println("�������� ��� ������");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubtaskList());

    }


}
