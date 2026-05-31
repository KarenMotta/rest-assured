import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class BookingEndpoint {

    private final String BASE_URL = "https://restful-booker.herokuapp.com";

    public String login() throws IOException {
        RestAssured.baseURI = BASE_URL;
        String jsonBody = lerJson("src/test/resources/payloads/login.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();

        return response.path("token");
    }

    public void buscarTodasReservas() {
        RestAssured.baseURI = BASE_URL;
        given()
                .header("Accept", "*/*")
                .when()
                .get("/booking/")
                .then()
                .statusCode(200)
                .log().all();
    }

    public void buscarReservaEspecifica() {
        RestAssured.baseURI = BASE_URL;
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/281")
                .then()
                .statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Smith"))
                .body("totalprice", equalTo(111))
                .body("depositpaid", is(true))
                .body("bookingdates.checkin", equalTo("2018-01-01"))
                .body("bookingdates.checkout", equalTo("2019-01-01"))
                .body("additionalneeds", equalTo("Breakfast"));
    }

    public String lerJson(String caminhoArquivo) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
    }

    public Integer cadastrarReserva() throws IOException {
        RestAssured.baseURI = BASE_URL;

        String jsonBody = lerJson("src/test/resources/payloads/reserva.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo("Karen"))
                .body("booking.lastname", equalTo("Motta"))
                .body("booking.totalprice", equalTo(300))
                .body("booking.depositpaid", is(true))
                .body("booking.bookingdates.checkin", equalTo("2026-06-01"))
                .body("booking.bookingdates.checkout", equalTo("2026-06-10"))
                .extract().response();

        return response.path("bookingid");
    }

    public void alterarReserva(Integer bookingId, String token) throws IOException {
        RestAssured.baseURI = BASE_URL;

        String jsonBody = lerJson("src/test/resources/payloads/reservaAlterada.json");
        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(jsonBody)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Karen"))
                .body("lastname", equalTo("Nova Reserva"))
                .body("totalprice", equalTo(300))
                .body("depositpaid", is(false))
                .body("bookingdates.checkin", equalTo("2027-07-01"))
                .body("bookingdates.checkout", equalTo("2027-07-07"))
                .body("additionalneeds", equalTo("secador de cabelo"));
    }

    public void deletarReserva(Integer bookingId, String token) throws IOException {
        RestAssured.baseURI = BASE_URL;
        given()
                .header("Accept", "*/*")
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(201)
                .log().all();
    }
}