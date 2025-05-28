package test;

import com.proyecto.gestionseriestv.controlador.PlataformaControl;
import com.proyecto.gestionseriestv.modelo.Plataforma;
import com.proyecto.gestionseriestv.modelo.PlataformasDao;
import com.proyecto.gestionseriestv.modelo.GestorBaseDatos; 

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Para ejecutar tests en un orden específico si es necesario
class PlataformaControlTest {

    private PlataformaControl plataformaControl;
    private PlataformasDao plataformaDao;
    private GestorBaseDatos gestorDB;

    // IDs y datos para los tests para mantener consistencia y poder limpiar
    private static final int TEST_ID_1 = 901;
    private static final String TEST_NOMBRE_1 = "PlTest1";
    private static final String TEST_PAIS_1 = "PT1";

    private static final int TEST_ID_2 = 902;
    private static final String TEST_NOMBRE_2 = "PlTest2";
    private static final String TEST_PAIS_2 = "PT2";
    
    private static final int TEST_ID_ACTUALIZAR = 903;


    @BeforeEach
    void setUp() {
        gestorDB = new GestorBaseDatos(); 
         gestorDB.verificarYCrearTablas(); 

        plataformaDao = new PlataformasDao(gestorDB);
        plataformaControl = new PlataformaControl(plataformaDao);

        plataformaDao.eliminar(TEST_ID_1);
        plataformaDao.eliminar(TEST_ID_2);
        plataformaDao.eliminar(TEST_ID_ACTUALIZAR);
    }
    
    @AfterEach
    void tearDown() {
        // Limpieza después de cada test para no dejar datos de prueba
        plataformaDao.eliminar(TEST_ID_1);
        plataformaDao.eliminar(TEST_ID_2);
        plataformaDao.eliminar(TEST_ID_ACTUALIZAR);
         gestorDB.cerrarConexion();
    }


    // --- Tests de validación  ---
    @Test
    @DisplayName("registrarPlataforma debería devolver false si el nombre es nulo")
    @Order(1)
    void registrarPlataforma_nombreNulo_devuelveFalse() {
        boolean resultado = plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_1), null, TEST_PAIS_1);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("registrarPlataforma debería devolver false si el nombre está vacío")
    @Order(2)
    void registrarPlataforma_nombreVacio_devuelveFalse() {
        boolean resultado = plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_1), "   ", TEST_PAIS_1);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("registrarPlataforma debería devolver false si el ID no es numérico")
    @Order(3)
    void registrarPlataforma_idNoNumerico_devuelveFalse() {
        boolean resultado = plataformaControl.registrarPlataforma("abc", TEST_NOMBRE_1, TEST_PAIS_1);
        assertFalse(resultado);
    }

    // --- Tests que interactúan con la BD ---
    @Test
    @DisplayName("registrarPlataforma con datos válidos debería ser exitoso y encontrarse en la BD")
    @Order(4)
    void registrarPlataforma_datosValidos_esExitosoYSeEncuentra() {
        boolean registrado = plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_1), TEST_NOMBRE_1, TEST_PAIS_1);
        assertTrue(registrado, "El registro debería ser exitoso");

        // Ahora verificamos que existe en la BD a través del controlador
        Plataforma recuperada = plataformaControl.obtenerPlataformaPorIdParaEdicion(TEST_ID_1);
        assertNotNull(recuperada, "La plataforma registrada debería encontrarse");
        assertEquals(TEST_NOMBRE_1, recuperada.getNombre());
        assertEquals(TEST_PAIS_1, recuperada.getPaisOrigen());
    }
    
    @Test
    @DisplayName("registrarPlataforma con ID duplicado debería fallar (si el DAO lo impide)")
    @Order(5)
    void registrarPlataforma_idDuplicado_deberiaFallar() {
        
        plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_1), TEST_NOMBRE_1, TEST_PAIS_1);
        
        // Intentamos registrar otra con el mismo ID
        boolean resultadoSegundoRegistro = plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_1), "Otro Nombre", "OTRO");
        assertFalse(resultadoSegundoRegistro, "No se debería poder registrar una plataforma con ID duplicado");
    }

    @Test
    @DisplayName("obtenerTodasLasPlataformasParaVista debería devolver plataformas registradas")
    @Order(6)
    void obtenerTodasLasPlataformas_devuelveRegistradas() {
        plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_1), TEST_NOMBRE_1, TEST_PAIS_1);
        plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_2), TEST_NOMBRE_2, TEST_PAIS_2);

        List<Plataforma> plataformas = plataformaControl.obtenerTodasLasPlataformasParaVista();
        assertNotNull(plataformas, "La lista de plataformas no debería ser nula");
        assertTrue(plataformas.size() >= 2, "Debería haber al menos 2 plataformas de test");

        boolean encontrada1 = false;
        boolean encontrada2 = false;
        for (Plataforma p : plataformas) {
            if (p.getId() == TEST_ID_1 && p.getNombre().equals(TEST_NOMBRE_1)) {
                encontrada1 = true;
            }
            if (p.getId() == TEST_ID_2 && p.getNombre().equals(TEST_NOMBRE_2)) {
                encontrada2 = true;
            }
        }
        assertTrue(encontrada1, "PlataformaTest1 (ID: " + TEST_ID_1 + ") no encontrada en la lista");
        assertTrue(encontrada2, "PlataformaTest2 (ID: " + TEST_ID_2 + ") no encontrada en la lista");
    }

    
    @Test
    @DisplayName("obtenerPlataformaPorIdParaEdicion cuando no existe debería devolver null")
    @Order(7)
    void obtenerPlataformaPorId_noExiste_devuelveNull() {
        Plataforma resultado = plataformaControl.obtenerPlataformaPorIdParaEdicion(99999); // Un ID que no debería existir
        assertNull(resultado);
    }

    @Test
    @DisplayName("actualizarPlataforma debería modificar una plataforma existente")
    @Order(8)
    void actualizarPlataforma_modificaExistente() {
        // Registrar una plataforma para actualizar
        plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_ACTUALIZAR), "Nombre Original", "Pais Original");

        String nuevoNombre = "Nombre Actualizado";
        String nuevoPais = "Pais Actualizado";
        boolean actualizado = plataformaControl.actualizarPlataforma(TEST_ID_ACTUALIZAR, nuevoNombre, nuevoPais);
        assertTrue(actualizado, "La actualización debería ser exitosa");

        Plataforma recuperada = plataformaControl.obtenerPlataformaPorIdParaEdicion(TEST_ID_ACTUALIZAR);
        assertNotNull(recuperada);
        assertEquals(nuevoNombre, recuperada.getNombre());
        assertEquals(nuevoPais, recuperada.getPaisOrigen());
    }
    
    @Test
    @DisplayName("actualizarPlataforma de ID inexistente debería fallar")
    @Order(9)
    void actualizarPlataforma_idInexistente_deberiaFallar() {
        boolean actualizado = plataformaControl.actualizarPlataforma(88888, "Nombre Fantasma", "Pais Fantasma");
        assertFalse(actualizado, "La actualización de un ID inexistente debería fallar");
    }


    @Test
    @DisplayName("eliminarPlataforma debería quitar una plataforma existente")
    @Order(10)
    void eliminarPlataforma_quitaExistente() {
        // Registrar una plataforma para eliminar
        plataformaControl.registrarPlataforma(Integer.toString(TEST_ID_1), TEST_NOMBRE_1, TEST_PAIS_1);
        
        // Verificar que existe antes de eliminar
        assertNotNull(plataformaControl.obtenerPlataformaPorIdParaEdicion(TEST_ID_1));

        boolean eliminado = plataformaControl.eliminarPlataforma(TEST_ID_1);
        assertTrue(eliminado, "La eliminación debería ser exitosa");

        // Verificar que ya no existe
        assertNull(plataformaControl.obtenerPlataformaPorIdParaEdicion(TEST_ID_1));
    }
    
    @Test
    @DisplayName("eliminarPlataforma de ID inexistente debería fallar (o devolver false)")
    @Order(11)
    void eliminarPlataforma_idInexistente_deberiaFallar() {
        boolean eliminado = plataformaControl.eliminarPlataforma(77777);
        assertFalse(eliminado, "La eliminación de un ID inexistente debería fallar o devolver false");
    }
}