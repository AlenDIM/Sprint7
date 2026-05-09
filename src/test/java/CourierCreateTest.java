import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.steps.CourierSteps;
import org.example.pojo.CourierCreateRequest;
import org.example.pojo.CourierLoginRequest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreateTest {

    public static String login = "drozhzhind";
    public static String password = "qwerty1234";
    public static String firstName = "Михаил";


    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверяем, что курьера можно создать с валидными данными")
    public void createNewCourier() {

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
        CourierSteps courierSteps = new CourierSteps();

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        courierSteps.courierDeleteAfterLogin(courierLoginRequest);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Попытка создать двух курьеров с одинаковым набором данных. Создание второго курьера должно провалиться")
    public void createTwoIdenticalCouriers() {

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
        CourierSteps courierSteps = new CourierSteps();

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);

        courierSteps.courierDeleteAfterLogin(courierLoginRequest);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Попытка создать курьера без передачи поля login. Создание курьера должно провалиться")
    public void createCourierWithoutLogin() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, null, firstName);
        CourierSteps courierSteps = new CourierSteps();

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Попытка создать курьера без передачи поля password. Создание курьера должно провалиться")
    public void createCourierWithoutPassword() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(null, password, firstName);
        CourierSteps courierSteps = new CourierSteps();

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}