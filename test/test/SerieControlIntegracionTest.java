package test; 

import com.proyecto.gestionseriestv.controlador.SerieControl;
import com.proyecto.gestionseriestv.modelo.*; 

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SerieControlIntegracionTest {

    private SerieControl serieControl;
    private SeriesDao seriesDao;
    private PlataformasDao plataformasDao; 
    private GestorBaseDatos gestorDB;

    // Datos de prueba para Plataforma
    private static final int PLAT_ID_1 = 101;
    private static final String PLAT_NOMBRE_1 = "TestPlatS";
    private static final String PLAT_PAIS_1 = "TPS";
    private Plataforma plataformaPrueba1;

    // Datos de prueba para Serie
    private static final int SERIE_ID_1 = 81;
    private static final String SERIE_TITULO_1 = "SerieTest1";
    private static final String SERIE_GENERO_1 = "Sci-Fi Test";
    private static final int SERIE_TEMP_1 = 3;
    private static final int SERIE_AÑO_1 = 2023;

    private static final int SERIE_ID_2 = 82;
    private static final String SERIE_TITULO_2 = "OtraSerieTest";
    
    private static final int SERIE_ID_ACTUALIZAR = 83;


    @BeforeEach
    void setUp() {
        gestorDB = new GestorBaseDatos();

        seriesDao = new SeriesDao(gestorDB);
        plataformasDao = new PlataformasDao(gestorDB); 
        serieControl = new SerieControl(seriesDao, plataformasDao);

        // Limpiar datos de prueba de series
        System.out.println("setUp: Eliminando series de prueba si existen...");
        seriesDao.eliminar(SERIE_ID_1);
        seriesDao.eliminar(SERIE_ID_2);
        seriesDao.eliminar(SERIE_ID_ACTUALIZAR);

        System.out.println("setUp: Eliminando y creando plataforma de prueba...");
        plataformasDao.eliminar(PLAT_ID_1);
        plataformaPrueba1 = new Plataforma(PLAT_ID_1, PLAT_NOMBRE_1, PLAT_PAIS_1);
        boolean platCreada = plataformasDao.registrar(plataformaPrueba1);
        assertTrue(platCreada, "La plataforma de prueba debe registrarse para los tests de series");
        System.out.println("setUp: Configuración completada.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown: Eliminando datos de prueba...");
        seriesDao.eliminar(SERIE_ID_1);
        seriesDao.eliminar(SERIE_ID_2);
        seriesDao.eliminar(SERIE_ID_ACTUALIZAR);
        plataformasDao.eliminar(PLAT_ID_1); // Elim
        System.out.println("tearDown: Datos de prueba eliminados.");
        if (gestorDB != null) {
            gestorDB.cerrarConexion();
        }
    }

    // --- Tests de validación del SerieControl ---
    @Test
    @Order(1)
    @DisplayName("registrarSerie debería devolver false si el título es nulo")
    void registrarSerie_tituloNulo_devuelveFalse() {
        boolean resultado = serieControl.registrarSerie(
                Integer.toString(SERIE_ID_1), null, SERIE_GENERO_1,
                Integer.toString(SERIE_TEMP_1), Integer.toString(SERIE_AÑO_1), plataformaPrueba1
        );
        assertFalse(resultado);
    }

    @Test
    @Order(2)
    @DisplayName("registrarSerie debería devolver false si la plataforma es nula")
    void registrarSerie_plataformaNula_devuelveFalse() {
        boolean resultado = serieControl.registrarSerie(
                Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1,
                Integer.toString(SERIE_TEMP_1), Integer.toString(SERIE_AÑO_1), null
        );
        assertFalse(resultado);
    }

    @Test
    @Order(3)
    @DisplayName("registrarSerie debería devolver false si el ID no es numérico")
    void registrarSerie_idNoNumerico_devuelveFalse() {
        boolean resultado = serieControl.registrarSerie(
                "abc", SERIE_TITULO_1, SERIE_GENERO_1,
                Integer.toString(SERIE_TEMP_1), Integer.toString(SERIE_AÑO_1), plataformaPrueba1
        );
        assertFalse(resultado);
    }
    
    @Test
    @Order(4)
    @DisplayName("registrarSerie debería devolver false si numTemporadas no es numérico")
    void registrarSerie_tempNoNumerica_devuelveFalse() {
        boolean resultado = serieControl.registrarSerie(
                Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1,
                "xyz", Integer.toString(SERIE_AÑO_1), plataformaPrueba1
        );
        assertFalse(resultado);
    }
    
    @Test
    @Order(5)
    @DisplayName("registrarSerie debería devolver false si año no es numérico")
    void registrarSerie_anioNoNumerico_devuelveFalse() {
        boolean resultado = serieControl.registrarSerie(
                Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1,
                Integer.toString(SERIE_TEMP_1), "uvw", plataformaPrueba1
        );
        assertFalse(resultado);
    }

    @Test
    @Order(6)
    @DisplayName("registrarSerie debería devolver false si numTemporadas es cero o negativo")
    void registrarSerie_tempInvalida_devuelveFalse() {
        boolean resultado = serieControl.registrarSerie(
                Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1,
                "0", Integer.toString(SERIE_AÑO_1), plataformaPrueba1
        );
        assertFalse(resultado, "Temporadas <= 0 deberían fallar");
    }
    
    @Test
    @Order(7)
    @DisplayName("registrarSerie debería devolver false si año es inválido")
    void registrarSerie_anioInvalido_devuelveFalse() {
        boolean resultado = serieControl.registrarSerie(
                Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1,
                Integer.toString(SERIE_TEMP_1), "1800", plataformaPrueba1
        );
        assertFalse(resultado, "Año <= 1900 debería fallar");
    }


    // --- Tests que interactúan con la BD ---
    @Test
    @Order(10)
    @DisplayName("registrarSerie con datos válidos debería ser exitoso")
    void registrarSerie_datosValidos_esExitoso() {
        boolean registrado = serieControl.registrarSerie(
                Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1,
                Integer.toString(SERIE_TEMP_1), Integer.toString(SERIE_AÑO_1), plataformaPrueba1
        );
        assertTrue(registrado, "El registro de la serie debería ser exitoso");

        Serie recuperada = serieControl.obtenerSeriePorIdParaEdicion(SERIE_ID_1);
        assertNotNull(recuperada, "La serie registrada debería encontrarse");
        assertEquals(SERIE_TITULO_1, recuperada.getTitulo());
        assertNotNull(recuperada.getPlataforma(), "La plataforma de la serie no debería ser nula");
        assertEquals(PLAT_ID_1, recuperada.getPlataforma().getId());
    }

    @Test
    @Order(11)
    @DisplayName("obtenerSeriePorIdParaEdicion cuando no existe debería devolver null")
    void obtenerSeriePorId_noExiste_devuelveNull() {
        Serie resultado = serieControl.obtenerSeriePorIdParaEdicion(99999);
        assertNull(resultado);
    }
    
    @Test
    @Order(12)
    @DisplayName("cargarTodasLasSeries debería devolver series registradas")
    void cargarTodasLasSeries_devuelveRegistradas() {
        serieControl.registrarSerie(Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1, Integer.toString(SERIE_TEMP_1), Integer.toString(SERIE_AÑO_1), plataformaPrueba1);
        serieControl.registrarSerie(Integer.toString(SERIE_ID_2), SERIE_TITULO_2, "Otro Genero", "1", "2024", plataformaPrueba1);

        List<Serie> series = serieControl.cargarTodasLasSeries();
        assertNotNull(series);
        assertTrue(series.size() >= 2, "Debería haber al menos 2 series de test");
        
        boolean encontrada1 = false;
        for(Serie s : series) {
            if(s.getId() == SERIE_ID_1) encontrada1 = true;
        }
        assertTrue(encontrada1, "SerieTest1 no encontrada");
    }


    @Test
    @Order(13)
    @DisplayName("actualizarSerie debería modificar una serie existente")
    void actualizarSerie_modificaExistente() {
        serieControl.registrarSerie(
            Integer.toString(SERIE_ID_ACTUALIZAR), "Título Original Serie", "Género Original",
            "1", "2020", plataformaPrueba1
        );

        String nuevoTitulo = "Título Serie Actualizado";
        boolean actualizado = serieControl.actualizarSerie(
            SERIE_ID_ACTUALIZAR, nuevoTitulo, "Género Nuevo",
            "2", "2021", plataformaPrueba1
        );
        assertTrue(actualizado, "La actualización de la serie debería ser exitosa");

        Serie recuperada = serieControl.obtenerSeriePorIdParaEdicion(SERIE_ID_ACTUALIZAR);
        assertNotNull(recuperada);
        assertEquals(nuevoTitulo, recuperada.getTitulo());
        assertEquals("Género Nuevo", recuperada.getGenero());
        assertEquals(2, recuperada.getNumeroTemporadas());
    }

    @Test
    @Order(14)
    @DisplayName("eliminarSerie debería quitar una serie existente")
    void eliminarSerie_quitaExistente() {
        serieControl.registrarSerie(
            Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1,
            Integer.toString(SERIE_TEMP_1), Integer.toString(SERIE_AÑO_1), plataformaPrueba1
        );
        assertNotNull(serieControl.obtenerSeriePorIdParaEdicion(SERIE_ID_1));

        boolean eliminado = serieControl.eliminarSerie(SERIE_ID_1);
        assertTrue(eliminado);

        assertNull(serieControl.obtenerSeriePorIdParaEdicion(SERIE_ID_1));
    }
    
    @Test
    @Order(15)
    @DisplayName("buscarSeries por TITULO debería devolver la serie correcta")
    void buscarSeries_porTitulo_devuelveCorrecta() {
        serieControl.registrarSerie(Integer.toString(SERIE_ID_1), "Serie Unica Busqueda", "Aventura", "1", "2024", plataformaPrueba1);
        serieControl.registrarSerie(Integer.toString(SERIE_ID_2), "Otra Serie Diferente", "Comedia", "2", "2024", plataformaPrueba1);

        List<Serie> encontradas = serieControl.buscarSeries("TITULO", "Unica Busqueda");
        assertNotNull(encontradas);
        assertEquals(1, encontradas.size());
        assertEquals(SERIE_ID_1, encontradas.get(0).getId());
    }

    @Test
    @Order(16)
    @DisplayName("buscarSeries por PLATAFORMA (nombre) debería devolver la serie correcta")
    void buscarSeries_porPlataformaNombre_devuelveCorrecta() {
        serieControl.registrarSerie(Integer.toString(SERIE_ID_1), SERIE_TITULO_1, SERIE_GENERO_1, Integer.toString(SERIE_TEMP_1), Integer.toString(SERIE_AÑO_1), plataformaPrueba1);
        
        // Crear otra plataforma y otra serie para asegurar que el filtro funciona
        Plataforma plataformaPrueba2 = new Plataforma(PLAT_ID_1 + 10, "PlatDistinta", "PDX");
        plataformasDao.registrar(plataformaPrueba2);
        serieControl.registrarSerie(Integer.toString(SERIE_ID_2), SERIE_TITULO_2, "Otro Gen", "1", "2021", plataformaPrueba2);


        List<Serie> encontradas = serieControl.buscarSeries("PLATAFORMA", PLAT_NOMBRE_1); 
        assertNotNull(encontradas);
        assertTrue(encontradas.size() >= 1, "Debería encontrar al menos una serie para la plataforma " + PLAT_NOMBRE_1);
        
        boolean todasDePlataformaCorrecta = true;
        for(Serie s : encontradas) {
            if(s.getPlataforma() == null || !s.getPlataforma().getNombre().equalsIgnoreCase(PLAT_NOMBRE_1)) {
                todasDePlataformaCorrecta = false;
                break;
            }
        }
        assertTrue(todasDePlataformaCorrecta, "Todas las series encontradas deben ser de la plataforma " + PLAT_NOMBRE_1);
        // Limpieza específica de la plataforma extra creada para este test
        plataformasDao.eliminar(PLAT_ID_1 + 10);
    }

}