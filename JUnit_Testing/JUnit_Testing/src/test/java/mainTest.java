import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class mainTest {

    @Test
    void comprobadorDeDNI_valido() {
        assertTrue(main.comprobadorDeDNI("12345678Z"), "El DNI debería ser válido.");
    }

    @Test
    void comprobadorDeDNI_longitudIncorrecta() {
        assertFalse(main.comprobadorDeDNI("1234567Z"), "El DNI no debería ser válido debido a longitud incorrecta.");
    }

    @Test
    void comprobadorDeDNI_letraIncorrecta() {
        assertFalse(main.comprobadorDeDNI("12345678A"), "El DNI no debería ser válido debido a una letra incorrecta.");
    }

    @Test
    void comprobadorDeDNI_caracteresInvalidos() {
        assertFalse(main.comprobadorDeDNI("12345A78Z"), "El DNI no debería ser válido debido a caracteres no numéricos.");
    }

    @Test
    void comprobadorDeDNI_dniVacio() {
        assertFalse(main.comprobadorDeDNI(""), "El DNI vacío no debería ser válido.");
    }
}
