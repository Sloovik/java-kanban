/**
 * Проверяем методы работы со списками дел.
 */

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Integer task1 = taskManager.add(new Task("Первый таск", "Убраться", StatusEnum.TODO));
        Integer task2 = taskManager.add(new Task("Второй таск", "Уехать за город", StatusEnum.TODO));

        Integer epic1 = taskManager.add(new Epic("Мой первый эпик", "Поступить в школу"));
        Integer subtask1 = taskManager.add(new Subtask("Первый сабтаск первого эпика", "Сдать экзамен",
                StatusEnum.TODO, epic1));
        Integer subtask2 = taskManager.add(new Subtask("Второй сабтаск первого эпика", "Помыться",
                StatusEnum.TODO, epic1));


        Integer epic2 = taskManager.add(new Epic("Второй эпик", "Переехать к подруге"));
        Integer subtask3 = taskManager.add(new Subtask("Первый сабтаск второго эпика", "Побриться",
                StatusEnum.TODO, epic2));

        taskManager.update(new Task(task1, "Новая первая таска", "Покормить пса",
                StatusEnum.IN_PROGRESS));
        taskManager.update(new Subtask(subtask1, "Новый сабтаск", "Купить корм черепахе",
                StatusEnum.DONE, epic1));
        taskManager.update(new Epic(epic2, "Новый второй эпик", "Купить кошку"));

        System.out.println("Печатаем все задачи");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubtaskList());

    }


}
