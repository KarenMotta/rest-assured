import io.restassured.RestAssured; //Biblioteca para automação de testes REST
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test; //Framework de testes JUnit 5
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.*;


//Classe que contém os testes de API
public class BookingTest {

    BookingEndpoint bookingEndpoint = new BookingEndpoint();

    public String lerJson(String caminhoArquivo) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
    }

    @Test
    public void consultaPageObjects() {
        bookingEndpoint.buscarTodasReservas();
    }

    @Test
    public void consultaPageObjectsComFiltro() {
        bookingEndpoint.buscarReservaEspecifica();
    }

    @Tag("smoke")
    @Test
    public void cadastrarReserva() throws IOException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String jsonBody = lerJson("src/test/resources/payloads/reserva.json");
        given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo("Karen5"))
                .body("booking.lastname", equalTo("Motta5"))
                .body("booking.totalprice", equalTo(300))
                .body("booking.depositpaid", is(true))
                .body("booking.bookingdates.checkin", equalTo("2026-06-01"))
                .body("booking.bookingdates.checkout", equalTo("2026-06-10"));
                //.body("booking.additionalneeds", equalTo("Breakfast"));
    }
    }