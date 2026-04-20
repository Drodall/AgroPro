import {
    UMBRALES_CANA_DE_AZUCAR,
    PLAGAS_CANA,
    FERTILIZACION_CANA,
    FACTORES_RENDIMIENTO_CANA,
} from './constantes-cana-azucar';
import { clienteAPI } from './servicios-api';
import type { Respuesta_API, Metricas_CanaDeAzucar, Prediccion_CanaDeAzucar, Analisis_Suelo_Cana, Plagas_CanaDeAzucar } from './servicios-api';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// ==================== SERVICIO ESPECIALIZADO PARA CAÑA DE AZÚCAR ====================

export class ServicioCaniaDeAzucar {
    private cliente = clienteAPI;

    /**
     * Obtiene todas las métricas específicas de caña de azúcar
     * ✅ ERROR 1 CORREGIDO: Usar hacerPeticion<T>() en lugar de ['hacer_peticion']()
     */
    async obtenerMetricasCaniaDeAzucar(idCampo: string): Promise<Respuesta_API<Metricas_CanaDeAzucar>> {
        return this.cliente.hacerPeticion<Metricas_CanaDeAzucar>(`/cana/metricas/${idCampo}`);
    }

    /**
     * Obtiene predicción específica para caña de azúcar
     */
    async obtenerPrediccionCaniaDeAzucar(idCampo: string): Promise<Respuesta_API<Prediccion_CanaDeAzucar>> {
        return this.cliente.hacerPeticion<Prediccion_CanaDeAzucar>(`/cana/prediccion/${idCampo}`);
    }

    /**
     * Realiza análisis de plagas específicas de caña de azúcar
     */
    async detectarPlagasCaniaDeAzucar(idCampo: string, imagen?: File): Promise<Respuesta_API<Plagas_CanaDeAzucar>> {
        if (imagen) {
            const formData = new FormData();
            formData.append('fieldId', idCampo);
            formData.append('image', imagen);

            const respuesta = await fetch(`${API_BASE_URL}/cana/plagas-detection`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('auth_token')}`,
                },
                body: formData,
            });

            if (!respuesta.ok) {
                throw new Error(`Error HTTP: ${respuesta.status}`);
            }

            return respuesta.json();
        }

        return this.cliente.hacerPeticion<Plagas_CanaDeAzucar>(`/cana/plagas/${idCampo}`);
    }

    /**
     * Análisis de suelo para caña de azúcar
     */
    async obtenerAnalisisSueloCaniaDeAzucar(idCampo: string): Promise<Respuesta_API<Analisis_Suelo_Cana>> {
        return this.cliente.hacerPeticion<Analisis_Suelo_Cana>(`/cana/analisis-suelo/${idCampo}`);
    }

    /**
     * Obtiene recomendación de fertilización personalizada
     */
    async obtenerRecomendacionFertilizacion(idCampo: string): Promise<Respuesta_API<{
        nitrogeno_kg_ha: number;
        fosforo_kg_ha: number;
        potasio_kg_ha: number;
        fechas_aplicacion: Date[];
        descripcion: string;
    }>> {
        return this.cliente.hacerPeticion<{
            nitrogeno_kg_ha: number;
            fosforo_kg_ha: number;
            potasio_kg_ha: number;
            fechas_aplicacion: Date[];
            descripcion: string;
        }>(`/cana/recomendacion-fertilizacion/${idCampo}`);
    }

    /**
     * Obtiene estimación de cosecha basada en IA
     */
    async estimarCosecha(idCampo: string): Promise<Respuesta_API<{
        fecha_optima: Date;
        toneladas_estimadas: number;
        brix_estimado: number;
        pol_estimado: number;
        calidad_azucar: number;
        dias_faltantes: number;
    }>> {
        return this.cliente.hacerPeticion<{
            fecha_optima: Date;
            toneladas_estimadas: number;
            brix_estimado: number;
            pol_estimado: number;
            calidad_azucar: number;
            dias_faltantes: number;
        }>(`/cana/estimacion-cosecha/${idCampo}`);
    }

    /**
     * Análisis de calidad del jugo (Brix, Pol, Pureza)
     */
    async obtenerAnalisisCalidadJugo(idCampo: string): Promise<Respuesta_API<{
        brix: number;
        pol: number;
        pureza: number;
        fibra: number;
        rendimiento_azucar: number;
        comparativa_optimo: string;
    }>> {
        return this.cliente.hacerPeticion<{
            brix: number;
            pol: number;
            pureza: number;
            fibra: number;
            rendimiento_azucar: number;
            comparativa_optimo: string;
        }>(`/cana/calidad-jugo/${idCampo}`);
    }

    /**
     * Recomendaciones sobre riego específicas para caña
     */
    async obtenerRecomendacionRiegoCaniaDeAzucar(idCampo: string): Promise<Respuesta_API<{
        proxima_fecha_riego: Date;
        cantidad_mm: number;
        justificacion: string;
        etapa_cultivo: string;
    }>> {
        return this.cliente.hacerPeticion<{
            proxima_fecha_riego: Date;
            cantidad_mm: number;
            justificacion: string;
            etapa_cultivo: string;
        }>(`/cana/recomendacion-riego/${idCampo}`);
    }

    /**
     * Historial y análisis de ciclos anteriores
     */
    async obtenerHistorialCiclos(idCampo: string): Promise<Respuesta_API<Array<{
        ciclo: string;
        fecha_corte: Date;
        toneladas_cosechadas: number;
        brix_promedio: number;
        pol_promedio: number;
        incidencia_plagas: string[];
    }>>> {
        return this.cliente.hacerPeticion<Array<{
            ciclo: string;
            fecha_corte: Date;
            toneladas_cosechadas: number;
            brix_promedio: number;
            pol_promedio: number;
            incidencia_plagas: string[];
        }>>(`/cana/historial-ciclos/${idCampo}`);
    }

    /**
     * Pronóstico del impacto de condiciones climáticas
     */
    async analizarImpactoClimatico(idCampo: string, pronostico_dias: number = 7): Promise<Respuesta_API<{
        temperatura_promedio: number;
        lluvia_esperada_mm: number;
        humedad_promedio: number;
        impacto_rendimiento: number; // Porcentaje
        recomendaciones: string[];
    }>> {
        return this.cliente.hacerPeticion<{
            temperatura_promedio: number;
            lluvia_esperada_mm: number;
            humedad_promedio: number;
            impacto_rendimiento: number;
            recomendaciones: string[];
        }>(`/cana/impacto-climatico/${idCampo}?dias=${pronostico_dias}`);
    }
}

// ==================== FUNCIONES AUXILIARES ====================

/**
 * Calcula el Brix esperado basado en edad del cultivo y condiciones
 */
export const calcularBrixEsperado = (
    diasDesdeEmergencia: number,
    temperatura_promedio: number,
    humedad_promedio: number
): number => {
    // Modelo simplificado
    const edad_factor = Math.min(diasDesdeEmergencia / 400, 1.0); // Máximo a 400 días
    const temp_factor = temperatura_promedio >= 20 && temperatura_promedio <= 30 ? 1.0 : 0.8;
    const hum_factor = humedad_promedio >= 65 && humedad_promedio <= 75 ? 1.0 : 0.85;

    return (12 + edad_factor * 8) * temp_factor * hum_factor;
};

/**
 * Determina el estado de severidad de plagas
 */
export const determinarSeveridadPlagas = (probabilidades: { [key: string]: number }): 'baja' | 'media' | 'alta' | 'critica' => {
    const promedio = Object.values(probabilidades).reduce((a, b) => a + b, 0) / Object.values(probabilidades).length;

    if (promedio > 80) return 'critica';
    if (promedio > 60) return 'alta';
    if (promedio > 40) return 'media';
    return 'baja';
};

/**
 * Calcula rendimiento esperado con múltiples factores
 */
export const calcularRendimientoEsperado = (
    diasDesdeEmergencia: number,
    temperatura_avg: number,
    humedad_avg: number,
    nutrientes_disponibles: { n: number; p: number; k: number },
    presencia_plagas: { [key: string]: number }
): number => {
    let rendimiento = FACTORES_RENDIMIENTO_CANA.base_rendimiento;

    // Factor edad
    const edad_factor = Math.min(diasDesdeEmergencia / 500, 1.0);
    rendimiento *= edad_factor;

    // Factor temperatura
    const temp_optimo = (UMBRALES_CANA_DE_AZUCAR.temperatura.optimo_min +
        UMBRALES_CANA_DE_AZUCAR.temperatura.optimo_max) / 2;
    const desviacion_temp = Math.abs(temperatura_avg - temp_optimo) / 10;
    const temp_factor = Math.max(0, 1 - desviacion_temp * 0.1);
    rendimiento *= temp_factor * FACTORES_RENDIMIENTO_CANA.temperatura_factor;

    // Factor humedad
    const hum_optimo = (UMBRALES_CANA_DE_AZUCAR.humedad_suelo.optimo_min +
        UMBRALES_CANA_DE_AZUCAR.humedad_suelo.optimo_max) / 2;
    const desviacion_hum = Math.abs(humedad_avg - hum_optimo) / 20;
    const hum_factor = Math.max(0, 1 - desviacion_hum * 0.15);
    rendimiento *= hum_factor * FACTORES_RENDIMIENTO_CANA.humedad_factor;

    // Factor nutrientes
    const nutrientes_factor = Math.min(
        (nutrientes_disponibles.n / 150) * 0.3 +
        (nutrientes_disponibles.p / 60) * 0.35 +
        (nutrientes_disponibles.k / 150) * 0.35,
        1.2
    );
    rendimiento *= nutrientes_factor * FACTORES_RENDIMIENTO_CANA.nutrientes_factor;

    // Factor plagas (reducción)
    const plagas_promedio = Object.values(presencia_plagas).reduce((a, b) => a + b, 0) / Object.values(presencia_plagas).length;
    const plagas_factor = Math.max(0.3, 1 - (plagas_promedio / 100) * 0.7);
    rendimiento *= plagas_factor * FACTORES_RENDIMIENTO_CANA.plagas_factor;

    return Math.round(rendimiento * 10) / 10; // Redondear a 1 decimal
};

/**
 * Obtiene recomendación de control para plaga específica
 */
export const obtenerControlPlaga = (nombrePlaga: string): string[] => {
    const plaga = PLAGAS_CANA[nombrePlaga as keyof typeof PLAGAS_CANA];
    return plaga ? plaga.control : ['Consultar con especialista agrícola'];
};

/**
 * Calcula días para cosecha óptima
 */
export const calcularDiasParaCosecha = (cicloVegetativo: string, diasActuales: number): number => {
    const ciclos: { [key: string]: number } = {
        plantilla: 540,
        resoca_1: 360,
        resoca_2: 360,
        resoca_3: 360,
        resoca_4: 360,
    };

    const diasTotales = ciclos[cicloVegetativo] || 360;
    return Math.max(0, diasTotales - diasActuales);
};

/**
 * Valida si los parámetros están dentro de rangos óptimos
 */
export const validarParametrosCaniaDeAzucar = (metricas: Metricas_CanaDeAzucar): {
    valido: boolean;
    problemas: string[];
    recomendaciones: string[];
} => {
    const problemas: string[] = [];
    const recomendaciones: string[] = [];

    // Validar temperatura
    if (metricas.temperatura) {
        if (metricas.temperatura < UMBRALES_CANA_DE_AZUCAR.temperatura.bajo) {
            problemas.push('Temperatura muy baja');
            recomendaciones.push('Esperar mejora de clima o usar protección');
        }
        if (metricas.temperatura > UMBRALES_CANA_DE_AZUCAR.temperatura.alto) {
            problemas.push('Temperatura muy alta');
            recomendaciones.push('Aumentar riego para reducir estrés por calor');
        }
    }

    // Validar humedad del suelo
    if (metricas.humedad_suelo) {
        if (metricas.humedad_suelo < UMBRALES_CANA_DE_AZUCAR.humedad_suelo.critico_bajo) {
            problemas.push('Estrés hídrico crítico');
            recomendaciones.push('RIEGA INMEDIATAMENTE');
        } else if (metricas.humedad_suelo < UMBRALES_CANA_DE_AZUCAR.humedad_suelo.bajo) {
            problemas.push('Humedad del suelo baja');
            recomendaciones.push('Programar riego en próximas 24 horas');
        }

        if (metricas.humedad_suelo > UMBRALES_CANA_DE_AZUCAR.humedad_suelo.encharcamiento) {
            problemas.push('Riesgo de encharcamiento');
            recomendaciones.push('Mejorar drenaje y evitar riego');
        }
    }

    // Validar pH del suelo
    if (metricas.ph_suelo) {
        if (metricas.ph_suelo < UMBRALES_CANA_DE_AZUCAR.ph_suelo.acido) {
            problemas.push('Suelo muy ácido');
            recomendaciones.push('Aplicar cal agrícola');
        }
        if (metricas.ph_suelo > UMBRALES_CANA_DE_AZUCAR.ph_suelo.alcalino) {
            problemas.push('Suelo muy alcalino');
            recomendaciones.push('Aplicar azufre elemental');
        }
    }

    // Validar Brix
    if (metricas.brix) {
        if (metricas.brix < UMBRALES_CANA_DE_AZUCAR.brix.bajo) {
            problemas.push('Brix bajo - azúcar insuficiente');
            recomendaciones.push('Revisar nutrición y riego');
        }
    }

    return {
        valido: problemas.length === 0,
        problemas,
        recomendaciones,
    };
};