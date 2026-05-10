import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.CourierCreateRequest;
import org.example.pojo.CourierLoginRequest;
import org.example.steps.CourierSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

public class CourierLoginTest {

    private final CourierSteps courierSteps = new CourierSteps();
    private final String login = "AlenWallke2r";
    private final String password = "qwerty12345";
    private final String firstName = "Дмитрий2";
    private int createdCourierId = 0;

    @Before
    public void createCourierForTests() {
        CourierCreateRequest createRequest = new CourierCreateRequest(login, password, firstName);
        courierSteps.courierCreate(createRequest)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        // Получаем ID созданного курьера для последующего удаления
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);
        Response response = courierSteps.courierLogin(loginRequest).extract().response();
        createdCourierId = response.jsonPath().getInt("id");
    }

    @After
    public void deleteCreatedCourier() {
        if (createdCourierId != 0) {
            courierSteps.courierDelete(createdCourierId);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Проверка, что курьер может авторизоваться с набором валидных данных")
    public void loginCourier() {
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);
        courierSteps.courierLogin(loginRequest)
                .statusCode(SC_OK)
                .body("id", instanceOf(Integer.class));
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Проверка, что курьер НЕ может авторизоваться без передачи поля login")
    public void loginCourierWithoutLogin() {
        CourierLoginRequest loginRequest = new CourierLoginRequest(null, password);
        courierSteps.courierLogin(loginRequest)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Проверка, что курьер НЕ может авторизоваться без передачи поля password")
    public void loginCourierWithoutPassword() {
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, null);
        courierSteps.courierLogin(loginRequest)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверка, что курьер НЕ может авторизоваться с несуществующим логином")
    public void loginWithWrongLogin() {
        CourierLoginRequest loginRequest = new CourierLoginRequest("wrong_login", password);
        courierSteps.courierLogin(loginRequest)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка, что курьер НЕ может авторизоваться с неверным паролем")
    public void loginWithWrongPassword() {
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, "wrong_password");
        courierSteps.courierLogin(loginRequest)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}