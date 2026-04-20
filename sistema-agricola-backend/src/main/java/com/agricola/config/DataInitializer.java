package com.agricola.config;

import com.agricola.model.*;
import com.agricola.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Inicializador de datos de demostración
 * Se ejecuta al arrancar la aplicación si la BD está vacía
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CampoRepository campoRepository;
    private final SensorRepository sensorRepository;
    private final DatosSensorRepository datosSensorRepository;
    private final MetricasCanaRepository metricasCanaRepository;
    private final CicloHistorialRepository cicloHistorialRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("✅ Base de datos ya inicializada. Saltando carga de datos demo.");
            return;
        }

        log.info("🌱 Inicializando datos de demostración para Sistema Agrícola...");

        // ===== USUARIOS =====
        Usuario admin = usuarioRepository.save(Usuario.builder()
            .nombre("Administrador Agrícola")
            .email("admin@agricola.com")
            .password(passwordEncoder.encode("Admin1234"))
            .rol(Usuario.RolUsuario.administrador)
            .camposAsignados(new ArrayList<>())
            .build());

        Usuario agricultor = usuarioRepository.save(Usuario.builder()
            .nombre("Carlos Mendoza")
            .email("carlos@agricola.com")
            .password(passwordEncoder.encode("Farmer2024"))
            .rol(Usuario.RolUsuario.agricultor)
            .camposAsignados(new ArrayList<>())
            .build());

        log.info("✅ Usuarios creados: admin@agricola.com / Admin1234");

        // ===== CAMPOS DE CAÑA =====
        Campo campoNorte = campoRepository.save(Campo.builder()
            .nombre("Parcela Norte - Caña RB867515")
            .area(45.5)
            .cultivo(Campo.TipoCultivo.cana_azucar)
            .latitud(4.7109)
            .longitud(-74.0721)
            .fechaSiembra(LocalDate.now().minusMonths(14))
            .estadoSalud(88)
            .variedad("RB867515")
            .cicloVegetativo(Campo.CicloVegetativo.plantilla)
            .toneladasCosechadasAnterior(0.0)
            .polEsperado(14.5)
            .build());

        Campo campoSur = campoRepository.save(Campo.builder()
            .nombre("Parcela Sur - Caña CP72-2086")
            .area(32.0)
            .cultivo(Campo.TipoCultivo.cana_azucar)
            .latitud(4.6890)
            .longitud(-74.0650)
            .fechaSiembra(LocalDate.now().minusMonths(26))
            .estadoSalud(75)
            .variedad("CP72-2086")
            .cicloVegetativo(Campo.CicloVegetativo.resoca_1)
            .fechaCorteAnterior(LocalDate.now().minusMonths(2))
            .toneladasCosechadasAnterior(78.5)
            .polEsperado(13.8)
            .build());

        Campo campoEste = campoRepository.save(Campo.builder()
            .nombre("Parcela Este - Caña RB72454")
            .area(28.0)
            .cultivo(Campo.TipoCultivo.cana_azucar)
            .latitud(4.7250)
            .longitud(-74.0580)
            .fechaSiembra(LocalDate.now().minusMonths(18))
            .estadoSalud(92)
            .variedad("RB72454")
            .cicloVegetativo(Campo.CicloVegetativo.resoca_2)
            .fechaCorteAnterior(LocalDate.now().minusMonths(14))
            .toneladasCosechadasAnterior(82.0)
            .polEsperado(15.0)
            .build());

        log.info("✅ 3 campos de caña de azúcar creados");

        // ===== SENSORES =====
        crearSensoresParaCampo(campoNorte, 4.7109, -74.0721);
        crearSensoresParaCampo(campoSur,   4.6890, -74.0650);
        crearSensoresParaCampo(campoEste,  4.7250, -74.0580);

        log.info("✅ Sensores e historial de lecturas creados");

        // ===== MÉTRICAS CAÑA =====
        crearMetricasCana(campoNorte.getIdCampo(), 25.5, 16.8, 13.9, 84.5, 12.1, 70.0, 6.4, 11.0, 185.0, 68.5);
        crearMetricasCana(campoSur.getIdCampo(),   26.2, 15.9, 13.2, 83.0, 12.5, 71.0, 6.6, 10.5, 175.0, 65.0);
        crearMetricasCana(campoEste.getIdCampo(),  24.8, 17.2, 14.1, 85.2, 11.8, 69.5, 6.3, 12.0, 195.0, 70.0);

        log.info("✅ Métricas de calidad de caña creadas");

        // ===== HISTORIAL CICLOS =====
        cicloHistorialRepository.save(CicloHistorial.builder()
            .idCampo(campoSur.getIdCampo())
            .ciclo("plantilla")
            .fechaCorte(LocalDate.now().minusMonths(14))
            .toneladasCosechadas(78.5)
            .brixPromedio(17.2)
            .polPromedio(14.1)
            .incidenciaPlagas("carbunclo")
            .build());

        cicloHistorialRepository.save(CicloHistorial.builder()
            .idCampo(campoEste.getIdCampo())
            .ciclo("resoca_1")
            .fechaCorte(LocalDate.now().minusMonths(2))
            .toneladasCosechadas(67.0)
            .brixPromedio(16.5)
            .polPromedio(13.5)
            .incidenciaPlagas("")
            .build());

        log.info("✅ Historial de ciclos creado");

        // ===== RECOMENDACIONES =====
        recomendacionRepository.save(Recomendacion.builder()
            .idCampo(campoNorte.getIdCampo())
            .tipo(Recomendacion.TipoRecomendacion.riego)
            .titulo("Riego preventivo programado")
            .descripcion("La humedad del suelo está en niveles óptimos. Programar próximo riego en 3 días.")
            .urgencia(Recomendacion.NivelUrgencia.baja)
            .accionesSugeridas("Verificar humedad mañana,Programar riego para el jueves,Revisar sistema de goteo")
            .build());

        recomendacionRepository.save(Recomendacion.builder()
            .idCampo(campoSur.getIdCampo())
            .tipo(Recomendacion.TipoRecomendacion.fertilizante)
            .titulo("Segunda aplicación de nitrógeno")
            .descripcion("Según el ciclo resoca_1, se recomienda aplicar nitrógeno a los 60 días del corte.")
            .urgencia(Recomendacion.NivelUrgencia.media)
            .accionesSugeridas("Aplicar 100 kg/ha de urea,Distribuir uniformemente,Regar después de aplicación")
            .build());

        log.info("✅ Recomendaciones iniciales creadas");
        log.info("🚀 Sistema inicializado correctamente. Backend listo en http://localhost:8080");
        log.info("📚 Swagger UI disponible en http://localhost:8080/swagger-ui.html");
        log.info("🔑 Credenciales demo: admin@agricola.com / Admin1234");
    }

    private void crearSensoresParaCampo(Campo campo, double lat, double lon) {
        // Sensor principal (temperatura + humedad)
        Sensor sensor = sensorRepository.save(Sensor.builder()
            .campo(campo)
            .estado(Sensor.EstadoSensor.activo)
            .latitud(lat)
            .longitud(lon)
            .nombre("Sensor Principal - " + campo.getNombre())
            .tipo("multiparametro")
            .build());

        // Generar 30 días de lecturas históricas
        LocalDateTime ahora = LocalDateTime.now();
        for (int i = 30; i >= 0; i--) {
            double temp      = 23 + (Math.random() * 6);    // 23-29°C
            double hum       = 65 + (Math.random() * 15);   // 65-80%
            double humSuelo  = 63 + (Math.random() * 12);   // 63-75%

            datosSensorRepository.save(DatosSensor.builder()
                .sensor(sensor)
                .temperatura(Math.round(temp * 10.0) / 10.0)
                .humedad(Math.round(hum * 10.0) / 10.0)
                .humedadSuelo(Math.round(humSuelo * 10.0) / 10.0)
                .fecha(ahora.minusDays(i).minusHours((long)(Math.random() * 5)))
                .build());
        }
    }

    private void crearMetricasCana(String idCampo, double temp, double brix, double pol,
                                    double pureza, double fibra, double humHoja,
                                    double ph, double densidad, double altura, double humSuelo) {
        metricasCanaRepository.save(MetricasCana.builder()
            .idCampo(idCampo)
            .fecha(LocalDateTime.now())
            .temperatura(temp)
            .brix(brix)
            .pol(pol)
            .pureza(pureza)
            .fibra(fibra)
            .humedadHoja(humHoja)
            .phSuelo(ph)
            .densidadTallos(densidad)
            .alturaPromedio(altura)
            .humedadSuelo(humSuelo)
            .build());
    }
}
