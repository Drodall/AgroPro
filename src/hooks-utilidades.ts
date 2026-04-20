// ==================== HOOKS PERSONALIZADOS ====================

import { useState, useEffect, useCallback } from 'react';
import { servicioIA, servicioSensores, servicioCampos } from './servicios-api';
import type {
    Respuesta_Predicciones,
    Datos_Sensor,
    Historial_Sensor,
} from './servicios-api';

/**
 * Hook para obtener predicciones de IA
 */
export const usePredicciones = (idCampo: string) => {
    const [predicciones, setPredicciones] = useState<Respuesta_Predicciones | null>(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const cargar = useCallback(async () => {
        try {
            setCargando(true);
            setError(null);
            const respuesta = await servicioIA.obtenerPredicciones(idCampo);
            if (respuesta.exito) {
                setPredicciones(respuesta.datos);
            } else {
                setError(respuesta.mensaje);
            }
        } catch (err) {
            setError('Error al cargar predicciones');
        } finally {
            setCargando(false);
        }
    }, [idCampo]);

    useEffect(() => {
        cargar();
        const intervalo = setInterval(cargar, 300000); // Actualizar cada 5 minutos
        return () => clearInterval(intervalo);
    }, [cargar]);

    return { predicciones, cargando, error, refrescar: cargar };
};

/**
 * Hook para obtener datos de sensores en tiempo real
 */
export const useSensoresEnTiempoReal = (idCampo: string, intervaloMs: number = 30000) => {
    const [sensores, setSensores] = useState<Datos_Sensor[]>([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const cargarSensores = async () => {
            try {
                setCargando(true);
                const respuesta = await servicioSensores.obtenerTodosSensores(idCampo);
                if (respuesta.exito) {
                    setSensores(respuesta.datos);
                } else {
                    setError(respuesta.mensaje);
                }
            } catch (err) {
                setError('Error al cargar sensores');
            } finally {
                setCargando(false);
            }
        };

        cargarSensores();
        const intervalo = setInterval(cargarSensores, intervaloMs);
        return () => clearInterval(intervalo);
    }, [idCampo, intervaloMs]);

    return { sensores, cargando, error };
};

/**
 * Hook para obtener historial de sensores
 */
export const useHistorialSensores = (idCampo: string, diasAnteriores: number = 30) => {
    const [historial, setHistorial] = useState<Historial_Sensor | null>(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const cargarHistorial = async () => {
            try {
                setCargando(true);
                const respuesta = await servicioSensores.obtenerHistorial(idCampo, diasAnteriores);
                if (respuesta.exito) {
                    setHistorial(respuesta.datos);
                } else {
                    setError(respuesta.mensaje);
                }
            } catch (err) {
                setError('Error al cargar historial');
            } finally {
                setCargando(false);
            }
        };

        cargarHistorial();
    }, [idCampo, diasAnteriores]);

    return { historial, cargando, error };
};

// ==================== UTILIDADES ====================

/**
 * Convierte valor de humedad/temperatura a estado visual
 * ✅ ERROR 3 CORREGIDO: Agregado default case
 */
export const obtenerColorEstado = (
    tipo: 'temperatura' | 'humedad' | 'humedad_suelo',
    valor: number
): string => {
    switch (tipo) {
        case 'temperatura':
            if (valor < 15) return 'text-blue-600';
            if (valor < 25) return 'text-green-600';
            if (valor < 35) return 'text-orange-600';
            return 'text-red-600';

        case 'humedad':
            if (valor < 30) return 'text-orange-600';
            if (valor < 60) return 'text-green-600';
            return 'text-blue-600';

        case 'humedad_suelo':
            if (valor < 20) return 'text-red-600';
            if (valor < 40) return 'text-orange-600';
            if (valor < 70) return 'text-green-600';
            return 'text-blue-600';

        // ✅ Default case para garantizar que siempre hay retorno
        default:
            return 'text-gray-600';
    }
};

/**
 * Obtiene icono según el tipo de sensor
 */
export const obtenerIconoSensor = (tipo: string): string => {
    const iconos: { [key: string]: string } = {
        temperatura: '🌡️',
        humedad: '💧',
        humedad_suelo: '🌱',
        luz: '☀️',
        presion: '🔽',
        default: '📊',
    };
    return iconos[tipo] || iconos['default'];
};

/**
 * Formatea fecha a formato legible
 */
export const formatearFecha = (fecha: Date, incluirHora: boolean = false): string => {
    const opciones: Intl.DateTimeFormatOptions = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        ...(incluirHora && { hour: '2-digit', minute: '2-digit' }),
    };
    return new Date(fecha).toLocaleDateString('es-ES', opciones);
};

/**
 * Calcula porcentaje visual
 */
export const calcularPorcentajeVisual = (valor: number, min: number, max: number): number => {
    return Math.min(Math.max((valor - min) / (max - min) * 100, 0), 100);
};

/**
 * Obtiene recomendación de color basada en urgencia
 */
export const obtenerColorUrgencia = (urgencia: 'baja' | 'media' | 'alta' | 'critica'): string => {
    const colores = {
        baja: 'bg-green-100 border-green-500 text-green-800',
        media: 'bg-yellow-100 border-yellow-500 text-yellow-800',
        alta: 'bg-orange-100 border-orange-500 text-orange-800',
        critica: 'bg-red-100 border-red-500 text-red-800',
    };
    return colores[urgencia];
};

// ==================== TIPOS DE ERROR ====================
export enum TipoError {
    SENSOR_DESCONECTADO = 'SENSOR_DESCONECTADO',
    PREDICCION_FALLIDA = 'PREDICCION_FALLIDA',
    RED_LENTA = 'RED_LENTA',
    AUTENTICACION_FALLIDA = 'AUTENTICACION_FALLIDA',
    PERMISO_DENEGADO = 'PERMISO_DENEGADO',
}

/**
 * Mapea códigos de error a mensajes amigables
 */
export const obtenerMensajeError = (tipo: TipoError): string => {
    const mensajes: { [key in TipoError]: string } = {
        [TipoError.SENSOR_DESCONECTADO]: 'El sensor está desconectado. Por favor verifica la conexión.',
        [TipoError.PREDICCION_FALLIDA]: 'No pudimos generar la predicción. Intenta más tarde.',
        [TipoError.RED_LENTA]: 'La conexión es lenta. Algunos datos pueden no ser precisos.',
        [TipoError.AUTENTICACION_FALLIDA]: 'Usuario o contraseña incorrectos.',
        [TipoError.PERMISO_DENEGADO]: 'No tienes permiso para acceder a este recurso.',
    };
    return mensajes[tipo];
};

// ==================== CONSTANTES ====================
export const UMBRALES = {
    temperatura: {
        min: 5,
        optimo_min: 15,
        optimo_max: 28,
        max: 45,
    },
    humedad: {
        min: 20,
        optimo_min: 40,
        optimo_max: 70,
        max: 100,
    },
    humedad_suelo: {
        critico_bajo: 15,
        bajo: 30,
        optimo_min: 40,
        optimo_max: 65,
        alto: 80,
        critico_alto: 90,
    },
};

export const INTERVALOS_ACTUALIZACION = {
    sensores_tiempo_real: 30000, // 30 segundos
    predicciones: 300000, // 5 minutos
    historial: 3600000, // 1 hora
};