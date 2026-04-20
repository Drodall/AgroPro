// ==================== TIPOS E INTERFACES ====================

/**
 * Respuesta estándar de la API
 */
interface Respuesta_API<T = any> {
    exito: boolean;
    mensaje: string;
    datos: T;
    timestamp?: string;
    codigo?: string;
    
}

/**
 * Usuario autenticado
 */
interface Usuario {
    id: string;
    nombre: string;
    apellido: string;
    email: string;
    telefono?: string;
    rol: 'admin' | 'agricultor' | 'supervisor';
    empresa?: string;
    fecha_creacion?: string;
}

/**
 * Sensor en el campo
 */
interface Sensor {
    sensorId: string;
    temperatura: number;
    humedad: number;
    humedad_suelo: number;
    ph_suelo?: number;
    fecha: string;
    estado: 'activo' | 'inactivo' | 'offline';
    ubicacion?: {
        latitud: number;
        longitud: number;
    };
}

/**
 * Campo agrícola
 */
interface Campo {
    id: string;
    nombre: string;
    ubicacion: {
        latitud: number;
        longitud: number;
    };
    area_hectareas: number;
    cultivo: 'cana' | 'maiz' | 'arroz' | 'soja';
    fecha_siembra: string;
    estado: 'activo' | 'inactivo' | 'cosechado';
    propietario_id: string;
    sensores?: Sensor[];
}

/**
 * Datos de sensor
 */
interface Datos_Sensor {
    id?: string;
    sensor_id: string;
    campo_id: string;
    temperatura: number;
    humedad_relativa: number;
    humedad_suelo: number;
    ph_suelo: number;
    nitrogeno: number;
    fosforo: number;
    potasio: number;
    presion_atmosferica?: number;
    velocidad_viento?: number;
    radiacion_solar?: number;
    timestamp: string;
}

/**
 * Historial de sensores
 */
interface Historial_Sensor {
    campo_id: string;
    periodo: {
        inicio: string;
        fin: string;
    };
    datos: Datos_Sensor[];
    promedios: {
        temperatura: number;
        humedad_relativa: number;
        humedad_suelo: number;
        ph_suelo: number;
    };
}

/**
 * Predicciones IA - Estructura completa
 */
interface Respuesta_Predicciones {
    campo_id: string;
    fecha_prediccion: string;
    cosecha: {
        rendimiento_estimado: number;
        confianza: number;
        fecha_estimada_cosecha: string;
        calidad: 'baja' | 'media' | 'alta' | 'excelente';
    };
    plagas: {
        probabilidad_plaga: number;
        tipo_plaga_detectada: string;
        severidad: 'baja' | 'media' | 'alta' | 'critica';
        control_recomendado: string[];
    };
    riego: {
        proxima_fecha_riego: string;
        cantidad_litros: number;
        duracion_minutos: number;
        justificacion: string;
    };
    clima_proximo: {
        temperatura_max: number;
        temperatura_min: number;
        probabilidad_lluvia: number;
        cantidad_lluvia_mm: number;
    };
}

/**
 * Predicción de cosecha
 */
interface Prediccion_Cosecha {
    campo_id: string;
    fecha_estimada_cosecha: string;
    rendimiento_estimado: number; // En toneladas
    calidad_estimada: 'baja' | 'media' | 'alta' | 'excelente';
    confianza: number; // Porcentaje 0-100
    factores_que_afectan: string[];
}

/**
 * Detección de plagas
 */
interface Deteccion_Plagas {
    campo_id: string;
    fecha_analisis: string;
    plagas_detectadas: {
        nombre: string;
        confianza: number;
        area_afectada_porcentaje: number;
        recomendaciones: string[];
    }[];
    severidad_general: 'baja' | 'media' | 'alta' | 'critica';
}

/**
 * Optimización de riego
 */
interface Optimizacion_Riego {
    campo_id: string;
    proxima_fecha_riego: string;
    cantidad_agua_mm: number;
    duracion_horas: number;
    justificacion: string;
    humedad_suelo_actual: number;
    humedad_suelo_optima: {
        minimo: number;
        maximo: number;
    };
}

/**
 * Análisis IA de planta
 */
interface Analisis_IA {
    campo_id: string;
    fecha_analisis: string;
    estado_general: 'excelente' | 'bueno' | 'regular' | 'malo';
    problemas_detectados: string[];
    indice_salud_vegetal: number; // 0-100
    recomendaciones: string[];
    confianza_analisis: number; // Porcentaje
}

/**
 * Recomendación del sistema
 */
interface Recomendacion {
    id: string;
    tipo: 'riego' | 'fertilizacion' | 'plagas' | 'cosecha' | 'otro';
    titulo: string;
    descripcion: string;
    urgencia: 'baja' | 'media' | 'alta' | 'critica';
    acciones_sugeridas: string[];
    fecha_recomendacion: string;
}

/**
 * Métricas de caña de azúcar
 */
interface Metricas_CanaDeAzucar {
    campo_id: string;
    temperatura: number;
    humedad_suelo: number;
    ph_suelo: number;
    brix: number;
    pol: number;
    pureza: number;
    nitrogeno: number;
    fosforo: number;
    potasio: number;
    dias_desde_emergencia: number;
}

/**
 * Predicción específica para caña de azúcar
 */
interface Prediccion_CanaDeAzucar {
    campo_id: string;
    fecha_prediccion: string;
    dias_para_cosecha: number;
    brix_estimado: number;
    toneladas_estimadas: number;
    calidad_azucar: number;
    riesgo_plagas: string[];
}

/**
 * Análisis de suelo para caña
 */
interface Analisis_Suelo_Cana {
    campo_id: string;
    ph: number;
    materia_organica: number;
    nitrogeno_disponible: number;
    fosforo_disponible: number;
    potasio_disponible: number;
    calcio: number;
    magnesio: number;
    azufre: number;
    recomendaciones_fertilizacion: string[];
}

/**
 * Plagas de caña de azúcar
 */
interface Plagas_CanaDeAzucar {
    campo_id: string;
    plagas_detectadas: {
        nombre: string;
        confianza: number;
        area_afectada: number;
        metodos_control: string[];
    }[];
    severidad: 'baja' | 'media' | 'alta' | 'critica';
}

// ==================== CONFIGURACIÓN API ====================
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

class ClienteAPI {
    private baseURL: string;
    private token: string | null = null;

    constructor(baseURL: string = API_BASE_URL) {
        this.baseURL = baseURL;
        this.token = localStorage.getItem('auth_token');
    }

    /**
     * Realiza una petición HTTP a la API
     */
    async hacerPeticion<T>(
        ruta: string,
        metodo: 'GET' | 'POST' | 'PUT' | 'DELETE' = 'GET',
        datos?: any
    ): Promise<Respuesta_API<T>> {
        const opciones: RequestInit = {
            method: metodo,
            headers: {
                'Content-Type': 'application/json',
                ...(this.token && { 'Authorization': `Bearer ${this.token}` }),
            },
        };

        if (datos && metodo !== 'GET') {
            opciones.body = JSON.stringify(datos);
        }

        try {
            const respuesta = await fetch(`${this.baseURL}${ruta}`, opciones);

            // Si el token expiró (401), limpiar y redirigir a login
            if (respuesta.status === 401) {
                this.limpiarToken();
                window.location.href = '/login';
            }

            if (!respuesta.ok) {
                throw new Error(`Error HTTP: ${respuesta.status} - ${respuesta.statusText}`);
            }

            const resultado: Respuesta_API<T> = await respuesta.json();
            return resultado;
        } catch (error) {
            console.error('Error en petición API:', error);
            throw error;
        }
    }

    /**
     * Establece el token de autenticación
     */
    setToken(token: string) {
        this.token = token;
        localStorage.setItem('auth_token', token);
    }

    /**
     * Limpia el token de autenticación
     */
    limpiarToken() {
        this.token = null;
        localStorage.removeItem('auth_token');
    }

    /**
     * Obtiene el token actual
     */
    obtenerToken(): string | null {
        return this.token;
    }
}

// ==================== SERVICIO DE SENSORES ====================
class ServicioSensores {
    private cliente: ClienteAPI;

    constructor(cliente: ClienteAPI) {
        this.cliente = cliente;
    }

    async obtenerDatos_Sensor(sensorId: string): Promise<Respuesta_API<Datos_Sensor>> {
        return this.cliente.hacerPeticion<Datos_Sensor>(`/sensors/${sensorId}`);
    }

    async obtenerDatos_CampoCompleto(idCampo: string): Promise<Respuesta_API<Campo>> {
        return this.cliente.hacerPeticion<Campo>(`/fields/${idCampo}`);
    }

    async obtenerTodosSensores(idCampo: string): Promise<Respuesta_API<Datos_Sensor[]>> {
        return this.cliente.hacerPeticion<Datos_Sensor[]>(`/fields/${idCampo}/sensors`);
    }

    async enviar_DatosSensor(datosSensor: Datos_Sensor): Promise<Respuesta_API<{ success: boolean }>> {
        return this.cliente.hacerPeticion<{ success: boolean }>('/sensors/data', 'POST', datosSensor);
    }

    async obtenerHistorial(idCampo: string, diasAnteriores: number = 30): Promise<Respuesta_API<Historial_Sensor>> {
        return this.cliente.hacerPeticion<Historial_Sensor>(`/sensors/history/${idCampo}?dias=${diasAnteriores}`);
    }
}

// ==================== SERVICIO IA Y PREDICCIONES ====================
class ServicioIA {
    private cliente: ClienteAPI;

    constructor(cliente: ClienteAPI) {
        this.cliente = cliente;
    }

    async obtenerPredicciones(idCampo: string): Promise<Respuesta_API<Respuesta_Predicciones>> {
        return this.cliente.hacerPeticion<Respuesta_Predicciones>(`/ai/predictions/${idCampo}`);
    }

    async predecirRendimiento(idCampo: string): Promise<Respuesta_API<Prediccion_Cosecha>> {
        return this.cliente.hacerPeticion<Prediccion_Cosecha>(`/ai/crop-prediction/${idCampo}`);
    }

    async detectarPlagas(idCampo: string, imagen?: File): Promise<Respuesta_API<Deteccion_Plagas>> {
        if (imagen) {
            const formData = new FormData();
            formData.append('fieldId', idCampo);
            formData.append('image', imagen);

            const respuesta = await fetch(`${API_BASE_URL}/ai/plague-detection`, {
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

        return this.cliente.hacerPeticion<Deteccion_Plagas>(`/ai/plague-detection/${idCampo}`);
    }

    async optimizarRiego(idCampo: string): Promise<Respuesta_API<Optimizacion_Riego>> {
        return this.cliente.hacerPeticion<Optimizacion_Riego>(`/ai/irrigation-optimization/${idCampo}`);
    }

    async obtenerRecomendaciones(idCampo: string): Promise<Respuesta_API<Recomendacion[]>> {
        return this.cliente.hacerPeticion<Recomendacion[]>(`/ai/recommendations/${idCampo}`);
    }

    async analizarImagenPlanta(idCampo: string, imagen: File): Promise<Respuesta_API<Analisis_IA>> {
        const formData = new FormData();
        formData.append('fieldId', idCampo);
        formData.append('image', imagen);

        const respuesta = await fetch(`${API_BASE_URL}/ai/analyze-plant`, {
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
}

// ==================== SERVICIO CAMPOS ====================
class ServicioCampos {
    private cliente: ClienteAPI;

    constructor(cliente: ClienteAPI) {
        this.cliente = cliente;
    }

    async obtenerTodosCampos(): Promise<Respuesta_API<Campo[]>> {
        return this.cliente.hacerPeticion<Campo[]>('/fields');
    }

    async obtenerCampo(idCampo: string): Promise<Respuesta_API<Campo>> {
        return this.cliente.hacerPeticion<Campo>(`/fields/${idCampo}`);
    }

    async crearCampo(campo: Campo): Promise<Respuesta_API<Campo>> {
        return this.cliente.hacerPeticion<Campo>('/fields', 'POST', campo);
    }

    async actualizarCampo(idCampo: string, actualizaciones: Partial<Campo>): Promise<Respuesta_API<Campo>> {
        return this.cliente.hacerPeticion<Campo>(`/fields/${idCampo}`, 'PUT', actualizaciones);
    }

    async eliminarCampo(idCampo: string): Promise<Respuesta_API<{ success: boolean }>> {
        return this.cliente.hacerPeticion<{ success: boolean }>(`/fields/${idCampo}`, 'DELETE');
    }
}

// ==================== SERVICIO AUTENTICACIÓN ====================
class ServicioAutenticacion {
    private cliente: ClienteAPI;

    constructor(cliente: ClienteAPI) {
        this.cliente = cliente;
    }

    async login(email: string, contrasena: string): Promise<Respuesta_API<{ token: string; usuario: Usuario }>> {
        const respuesta = await this.cliente.hacerPeticion<{ token: string; usuario: Usuario }>(
            '/auth/login',
            'POST',
            { email, contrasena }
        );

        if (respuesta.exito && respuesta.datos.token) {
            this.cliente.setToken(respuesta.datos.token);
        }

        return respuesta;
    }

    async logout(): Promise<void> {
        try {
            await this.cliente.hacerPeticion<void>('/auth/logout', 'POST');
        } catch (error) {
            console.error('Error al cerrar sesión:', error);
        } finally {
            this.cliente.limpiarToken();
        }
    }

    async registrarse(usuario: Partial<Usuario>, contrasena: string): Promise<Respuesta_API<Usuario>> {
        return this.cliente.hacerPeticion<Usuario>(
            '/auth/register',
            'POST',
            { usuario, contrasena }
        );
    }

    async obtenerPerfilActual(): Promise<Respuesta_API<Usuario>> {
        return this.cliente.hacerPeticion<Usuario>('/auth/profile');
    }

    async actualizarPerfil(actualizaciones: Partial<Usuario>): Promise<Respuesta_API<Usuario>> {
        return this.cliente.hacerPeticion<Usuario>(
            '/auth/profile',
            'PUT',
            actualizaciones
        );
    }

    async cambiarContrasena(contrasenaActual: string, contrasenaNueva: string): Promise<Respuesta_API<{ success: boolean }>> {
        return this.cliente.hacerPeticion<{ success: boolean }>(
            '/auth/change-password',
            'POST',
            { contrasena_actual: contrasenaActual, contrasena_nueva: contrasenaNueva }
        );
    }
}

// ==================== EXPORTAR SINGLETON ====================
export const clienteAPI = new ClienteAPI();
export const servicioSensores = new ServicioSensores(clienteAPI);
export const servicioIA = new ServicioIA(clienteAPI);
export const servicioCampos = new ServicioCampos(clienteAPI);
export const servicioAutenticacion = new ServicioAutenticacion(clienteAPI);

// ==================== EXPORTAR TIPOS ====================
export type {
    Respuesta_API,
    Usuario,
    Campo,
    Sensor,
    Datos_Sensor,
    Historial_Sensor,
    Respuesta_Predicciones,
    Prediccion_Cosecha,
    Deteccion_Plagas,
    Optimizacion_Riego,
    Analisis_IA,
    Recomendacion,
    Metricas_CanaDeAzucar,
    Prediccion_CanaDeAzucar,
    Analisis_Suelo_Cana,
    Plagas_CanaDeAzucar,
};
