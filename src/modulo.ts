// ==================== INTERFACES SENSORES ====================
interface Datos_Sensor {
    sensorId: string;
    temperatura: number;
    humedad: number;
    humedad_suelo: number;
    fecha: Date;
    localizacion: Coordenadas;
    idCampo: string;
    estado: 'activo' | 'inactivo' | 'error';
}

interface Coordenadas {
    latitud: number;
    longitud: number;
}

// ==================== INTERFACES CAMPOS ====================
interface Campo {
    idCampo: string;
    nombre: string;
    area: number; // hectáreas
    cultivo: 'maíz' | 'trigo' | 'arroz' | 'papa' | 'caña_azucar' | 'otro';
    ubicacion: Coordenadas;
    fecha_siembra: Date;
    sensores: Datos_Sensor[];
    estado_salud: number; // 0-100
}

// ==================== INTERFACES ESPECÍFICAS PARA CAÑA DE AZÚCAR ====================
interface CampoCaniaDeAzucar extends Campo {
    cultivo: 'caña_azucar';
    variedad: string; // Ej: 'CP72-2086', 'RB867515', 'RB72454'
    ciclo_vegetativo: 'plantilla' | 'resoca_1' | 'resoca_2' | 'resoca_3' | 'resoca_4';
    fecha_corte_anterior: Date | null;
    toneladas_cosechadas_anterior: number;
    pol_esperado: number; // Polarización esperada (0-20%)
}

interface Metricas_CanaDeAzucar {
    idCampo: string;
    fecha: Date;
    temperatura: number; // °C
    brix: number; // Sólidos solubles totales (12-22%)
    pol: number; // Sacarosa (10-18%)
    pureza: number; // % (75-90%)
    fibra: number; // % (10-15%)
    humedad_hoja: number; // % (65-75%)
    ph_suelo: number; // (5.5-7.5)
    densidad_tallos: number; // tallos/m²
    altura_promedio: number; // cm
    humedad_suelo: number; // % (capacidad de campo)
}

interface Prediccion_CanaDeAzucar extends Prediccion_Cosecha {
    brix_esperado: number;
    pol_esperado: number;
    toneladas_esperadas: number; // Toneladas de caña por hectárea
    azucar_estimada: number; // Kg de azúcar por tonelada de caña
    dias_para_cosecha: number;
}

interface Plagas_CanaDeAzucar {
    idCampo: string;
    plagas_detectadas: {
        chicharra: number; // 0-100
        minador: number;
        gusano_cogollero: number;
        gusano_pegador: number;
        nematodo_tallo: number;
        hongos_foliares: number;
        carbunclo: number; // Enfermedad bacteriana
        pokkah_boeng: number; // Enfermedad por estrés
    };
    nivel_critico: boolean;
    recomendaciones_plagas: string[];
    fecha_deteccion: Date;
}

interface Analisis_Suelo_Cana {
    idCampo: string;
    nitrogeno: number; // mg/kg
    fosforo: number; // mg/kg
    potasio: number; // mg/kg
    calcio: number; // mg/kg
    magnesio: number; // mg/kg
    azufre: number; // mg/kg
    micronutrientes: {
        hierro: number;
        manganeso: number;
        zinc: number;
        boro: number;
        cobre: number;
    };
    recomendacion_fertilizacion: {
        nitrogeno_kg_ha: number;
        fosforo_kg_ha: number;
        potasio_kg_ha: number;
    };
}

// ==================== INTERFACES PREDICCIONES IA ====================
interface Prediccion_Cosecha {
    idCampo: string;
    rendimiento_esperado: number; // kg/hectárea
    confianza: number; // 0-100%
    fecha_prediccion: Date;
    fecha_cosecha_estimada: Date;
    factores: {
        clima: number;
        suelo: number;
        agua: number;
        plagas: number;
    };
}

interface Deteccion_Plagas {
    idCampo: string;
    probabilidad_plaga: number; // 0-100%
    tipo_plaga_detectada: string;
    severidad: 'baja' | 'media' | 'alta';
    recomendacion: string;
    fecha_deteccion: Date;
    imagen_analizada: string; // URL o base64
}

interface Optimizacion_Riego {
    idCampo: string;
    humedad_optima: number;
    proxima_fecha_riego: Date;
    duracion_minutos: number;
    cantidad_litros: number;
    confianza: number;
}

// ==================== INTERFACES RECOMENDACIONES ====================
interface Recomendacion {
    id: string;
    idCampo: string;
    tipo: 'riego' | 'fertilizante' | 'plagas' | 'cosecha' | 'general';
    titulo: string;
    descripcion: string;
    urgencia: 'baja' | 'media' | 'alta' | 'critica';
    fecha_generada: Date;
    acciones_sugeridas: string[];
}

// ==================== INTERFACES RESPUESTAS API ====================
interface Respuesta_API<T> {
    exito: boolean;
    datos: T;
    mensaje: string;
    codigo_error?: number;
}

interface Respuesta_Predicciones {
    cosecha: Prediccion_Cosecha;
    plagas: Deteccion_Plagas;
    riego: Optimizacion_Riego;
    recomendaciones: Recomendacion[];
}

// ==================== INTERFACES USUARIO ====================
interface Usuario {
    idUsuario: string;
    nombre: string;
    email: string;
    rol: 'administrador' | 'agricultor' | 'tecnico';
    campos_asignados: string[]; // IDs de campos
    fecha_registro: Date;
}

// ==================== INTERFACES ANÁLISIS HISTÓRICOS ====================
interface Historial_Sensor {
    idCampo: string;
    fecha_inicio: Date;
    fecha_fin: Date;
    datos_promedio: {
        temperatura_avg: number;
        humedad_avg: number;
        humedad_suelo_avg: number;
    };
    tendencias: {
        temperatura: 'aumentando' | 'disminuyendo' | 'estable';
        humedad: 'aumentando' | 'disminuyendo' | 'estable';
    };
}

// ==================== TIPOS DE RESPUESTA IA ====================
interface Analisis_IA {
    tipo_analisis: 'prediccion' | 'deteccion' | 'optimizacion' | 'clasificacion';
    confianza_modelo: number;
    modelo_utilizado: string;
    version_modelo: string;
    tiempo_procesamiento_ms: number;
}