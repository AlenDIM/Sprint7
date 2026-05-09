package org.example.constants;

public class ApiEndpoint {
    public static String BASE_URL = "http://qa-scooter.praktikum-services.ru/";
    public static String COURIER_POST_CREATE = "/api/v1/courier"; // Создание курьера
    public static String COURIER_POST_LOGIN = "/api/v1/courier/login"; // Логин курьера
    public static String COURIER_DELETE = "/api/v1/courier/"; // Удаление курьера
    public static String ORDER_POST_CREATE = "/api/v1/orders"; // Создание заказа
    public static String ORDER_GET_LIST = "/api/v1/orders"; // Получение списка заказов
}
