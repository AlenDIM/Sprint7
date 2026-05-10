import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.steps.CourierSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.CourierCreateRequest;
import org.example.pojo.CourierLoginRequest;
import org.example.steps.CourierSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.example.constants.ApiEndpoint.BASE_URL;
import static org.example.constants.ApiEndpoint.COURIER_POST_LOGIN;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreateTest {

    private final CourierSteps courierSteps = new CourierSteps();
    private final String login = "AlenWallker2";
    private final String password = "qwerty12345";
    private final String firstName = "Дмитрий2";
    private int createdCourierId = 0;

    @Before
    public void deleteIfExists() {
        try {
            CourierLoginRequest loginReq = new CourierLoginRequest(login, password);
            Response resp = given()
                    .contentType(io.restassured.http.ContentType.JSON)
                    .body(loginReq)
                    .post(BASE_URL + COURIER_POST_LOGIN);
            if (resp.statusCode() == SC_OK) {
                int id = resp.jsonPath().getInt("id");
                given().delete(BASE_URL + "/api/v1/courier/" + id);
            }
        } catch (Exception e) {
            // Курьер не существует – ничего не делаем
        }
    }

    @After
    public void deleteCreatedCourier() {
        if (createdCourierId != 0) {
            courierSteps.courierDelete(createdCourierId);
        }
    }

    private void loginAndSaveId() {
        CourierLoginRequest loginReq = new CourierLoginRequest(login, password);
        Response response = courierSteps.courierLogin(loginReq).extract().response();
        createdCourierId = response.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверяем, что курьера можно создать с валидными данными")
    public void createNewCourier() {
        CourierCreateRequest request = new CourierCreateRequest(login, password, firstName);
        courierSteps.courierCreate(request)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        loginAndSaveId();
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Попытка создать двух курьеров с одинаковым набором данных. Создание второго курьера должно провалиться")
    public void createTwoIdenticalCouriers() {
        CourierCreateRequest request = new CourierCreateRequest(login, password, firstName);

        // Первое создание – успех
        courierSteps.courierCreate(request)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        // Второе создание – конфликт
        courierSteps.courierCreate(request)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        loginAndSaveId();
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Попытка создать курьера без передачи поля login. Создание курьера должно провалиться")
    public void createCourierWithoutLogin() {
        CourierCreateRequest request = new CourierCreateRequest(null, password, firstName);
        courierSteps.courierCreate(request)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Попытка создать курьера без передачи поля password. Создание курьера должно провалиться")
    public void createCourierWithoutPassword() {
        CourierCreateRequest request = new CourierCreateRequest(login, null, firstName);
        courierSteps.courierCreate(request)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}