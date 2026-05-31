import io.restassured.RestAssured; //Biblioteca para automação de testes REST
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test; //Framework de testes JUnit 5
import java.io.IOException;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.*;

//Classe que contém os testes de API
public class BookingTest {

    BookingEndpoint bookingEndpoint = new BookingEndpoint();

    @Test
    public void buscarTodasReservas() {
        bookingEndpoint.buscarTodasReservas();
    }

    @Test
    public void buscarReservaEspecifica() {
        bookingEndpoint.buscarReservaEspecifica();
    }

    @Test
    public void cadastrarReserva() throws IOException {
        bookingEndpoint.cadastrarReserva();
    }

    @Test
    public void cadastrarEAlterarReserva() throws IOException {
        // Faz login e captura o token
        String token = bookingEndpoint.login();

        // Cadastra uma nova reserva e captura o ID
        Integer bookingId = bookingEndpoint.cadastrarReserva();

        // Usa o ID e o token capturados para alterar a reserva
        bookingEndpoint.alterarReserva(bookingId, token);
    }

    @Test
    public void deletarReserva() throws IOException {
        // Faz login e capture o token
        String token = bookingEndpoint.login();

        //Cadastra uma nova reserva e captura o ID
        Integer bookingId = bookingEndpoint.cadastrarReserva();

        // Usa o ID e o token capturados para deletar a reserva
        bookingEndpoint.deletarReserva(bookingId, token);
    }
}