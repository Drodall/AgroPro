// ==================== UMBRALES OPTIMIZADOS PARA CAÑA DE AZÚCAR ====================

export const UMBRALES_CANA_DE_AZUCAR = {
    // Temperatura (°C) - Caña de azúcar: 20-30°C óptimo
    temperatura: {
        critico_bajo: 10,
        bajo: 15,
        optimo_min: 20,
        optimo_max: 30,
        alto: 35,
        critico_alto: 40,
    },

    // Humedad relativa (%) - 60-80% óptimo
    humedad_aire: {
        muy_baja: 30,
        baja: 50,
        optimo_min: 60,
        optimo_max: 80,
        alta: 90,
    },

    // Humedad del suelo (%) - 65-75% capacidad de campo
    humedad_suelo: {
        critico_bajo: 40, // Estrés hídrico
        bajo: 55,
        optimo_min: 65,
        optimo_max: 75,
        alto: 85,
        encharcamiento: 95,
    },

    // pH del suelo - 5.5-7.5 es óptimo
    ph_suelo: {
        muy_acido: 4.5,
        acido: 5.0,
        optimo_min: 5.5,
        optimo_max: 7.5,
        alcalino: 8.0,
        muy_alcalino: 8.5,
    },

    // Radiación solar (kWh/m²/día)
    radiacion: {
        baja: 12,
        normal: 16,
        optimo: 18,
        alta: 22,
    },

    // Precipitación (mm/mes)
    precipitacion: {
        escasez: 50,
        baja: 80,
        optimo_min: 100,
        optimo_max: 200,
        exceso: 300,
    },

    // Métricas específicas de calidad
    brix: {
        bajo: 12,
        aceptable: 14,
        optimo_min: 15,
        optimo_max: 20,
        alto: 22,
    },

    pol: {
        bajo: 10,
        aceptable: 12,
        optimo_min: 13,
        optimo_max: 18,
        muy_alto: 20,
    },

    pureza: {
        baja: 75,
        aceptable: 80,
        optimo_min: 82,
        optimo_max: 88,
        excelente: 90,
    },

    densidad_tallos: {
        bajo: 6,
        aceptable: 8,
        optimo_min: 10,
        optimo_max: 15,
        muy_alto: 18,
    },

    // Nitrógeno disponible (mg/kg)
    nitrogeno: {
        deficiencia: 20,
        bajo: 50,
        adecuado: 80,
        optimo_min: 100,
        optimo_max: 150,
        exceso: 200,
    },

    // Fósforo disponible (mg/kg)
    fosforo: {
        deficiencia: 10,
        bajo: 20,
        adecuado: 40,
        optimo_min: 50,
        optimo_max: 100,
        exceso: 150,
    },

    // Potasio disponible (mg/kg)
    potasio: {
        deficiencia: 50,
        bajo: 100,
        adecuado: 150,
        optimo_min: 200,
        optimo_max: 300,
        exceso: 400,
    },
};

// ==================== PLAGAS PRINCIPALES DE CAÑA DE AZÚCAR ====================
export const PLAGAS_CANA = {
    chicharra: {
        nombre: 'Chicharra (Mahanarva fimbriolata)',
        descripcion: 'Insecto que succiona savia, causa debilitamiento',
        riesgo_alto_temperatura: { min: 22, max: 30 },
        riesgo_alto_humedad: { min: 70, max: 90 },
        epoca_peligro: ['octubre', 'noviembre', 'diciembre'],
        control: [
            'Aplicar insecticidas sistémicos',
            'Mantener humedad del suelo adecuada',
            'Eliminar residuos de cosecha',
        ],
    },

    minador: {
        nombre: 'Minador de Hojas (Telchin licus)',
        descripcion: 'Larva perfora y excava galerías en las hojas',
        riesgo_alto_temperatura: { min: 20, max: 28 },
        riesgo_alto_humedad: { min: 65, max: 85 },
        epoca_peligro: ['septiembre', 'octubre', 'noviembre'],
        control: [
            'Monitoreo de capturas con trampas de luz',
            'Aplicar nematodos entomopatógenos',
            'Evitar estrés hídrico en plantas',
        ],
    },

    gusano_cogollero: {
        nombre: 'Gusano Cogollero (Diatraea saccharalis)',
        descripcion: 'Taladrador que ataca el cogollo y tallos',
        riesgo_alto_temperatura: { min: 24, max: 32 },
        riesgo_alto_humedad: { min: 60, max: 80 },
        epoca_peligro: ['agosto', 'septiembre', 'octubre', 'noviembre'],
        control: [
            'Usar variedades resistentes',
            'Aplicar Bacillus thuringiensis',
            'Control químico en fase crítica',
        ],
    },

    gusano_pegador: {
        nombre: 'Gusano Pegador (Erinnyis ello)',
        descripcion: 'Come hojas causando defoliación severa',
        riesgo_alto_temperatura: { min: 22, max: 30 },
        riesgo_alto_humedad: { min: 65, max: 85 },
        epoca_peligro: ['noviembre', 'diciembre', 'enero'],
        control: [
            'Recolección manual',
            'Aplicar insecticidas piretroides',
            'Mantener monitoreo constante',
        ],
    },

    nematodo_tallo: {
        nombre: 'Nematodo del Tallo (Meloidogyne spp)',
        descripcion: 'Debilita raíces, reduce absorción de nutrientes',
        riesgo_alto_temperatura: { min: 18, max: 28 },
        riesgo_alto_humedad: { min: 70, max: 90 },
        epoca_peligro: ['marzo', 'abril', 'mayo'],
        control: [
            'Rotación de cultivos',
            'Uso de nematicidas biológicos',
            'Selección de variedades tolerantes',
        ],
    },

    carbunclo: {
        nombre: 'Carbunclo (Xanthomonas albilineans)',
        descripcion: 'Enfermedad vascular bacteriana grave',
        riesgo_alto_temperatura: { min: 20, max: 28 },
        riesgo_alto_humedad: { min: 75, max: 95 },
        epoca_peligro: ['todo_el_año'],
        control: [
            'Usar material de siembra certificado',
            'Eliminar plantas infectadas',
            'Control de malezas hospederas',
        ],
    },

    pokkah_boeng: {
        nombre: 'Pokkah Boeng (Gibberella fujikuroi)',
        descripcion: 'Enfermedad fúngica por estrés hídrico',
        riesgo_alto_temperatura: { min: 22, max: 28 },
        riesgo_alto_humedad: { min: 80, max: 95 },
        epoca_peligro: ['lluvia_excesiva'],
        control: [
            'Mejorar drenaje del terreno',
            'Aplicar fungicidas preventivos',
            'Evitar exceso de humedad',
        ],
    },

    roya: {
        nombre: 'Roya de la Caña (Puccinia melanocephala)',
        descripcion: 'Enfermedad fúngica que reduce fotosíntesis',
        riesgo_alto_temperatura: { min: 18, max: 26 },
        riesgo_alto_humedad: { min: 85, max: 95 },
        epoca_peligro: ['junio', 'julio', 'agosto'],
        control: [
            'Usar variedades resistentes',
            'Aplicar fungicidas sistémicos',
            'Evitar altas densidades de plantación',
        ],
    },
};

// ==================== RECOMENDACIONES DE FERTILIZACIÓN PARA CAÑA ====================
export const FERTILIZACION_CANA = {
    plantilla: {
        descripcion: 'Primer ciclo de la caña',
        nitrogeno_kg_ha: 120,
        fosforo_kg_ha: 60,
        potasio_kg_ha: 80,
        aplicaciones: 2,
        descripcion_aplicacion: 'Una a los 45 días y otra a los 90 días',
    },

    resoca: {
        descripcion: 'Ciclos posteriores al corte',
        nitrogeno_kg_ha: 100,
        fosforo_kg_ha: 40,
        potasio_kg_ha: 60,
        aplicaciones: 1,
        descripcion_aplicacion: 'A los 60 días después del corte',
    },
};

// ==================== ÉPOCAS DE COSECHA ====================
export const EPOCAS_COSECHA = {
    optima_inicio: 'julio',
    optima_fin: 'noviembre',
    dias_maduracion: { min: 18, max: 24 }, // meses desde siembra
};

// ==================== CICLOS VEGETATIVOS ====================
export const CICLOS_CANA = {
    plantilla: {
        duracion_meses: 18,
        rendimiento_esperado: 80, // toneladas/hectárea
        descripcion: 'Siembra en abril, cosecha en octubre-noviembre',
    },
    resoca_1: {
        duracion_meses: 12,
        rendimiento_esperado: 65,
        descripcion: 'Primer rebrote',
    },
    resoca_2: {
        duracion_meses: 12,
        rendimiento_esperado: 55,
        descripcion: 'Segundo rebrote',
    },
    resoca_3: {
        duracion_meses: 12,
        rendimiento_esperado: 45,
        descripcion: 'Tercer rebrote',
    },
    resoca_4: {
        duracion_meses: 12,
        rendimiento_esperado: 35,
        descripcion: 'Cuarto rebrote (generalmente último)',
    },
};

// ==================== VARIEDADES RECOMENDADAS ====================
export const VARIEDADES_CANA = [
    {
        nombre: 'RB867515',
        precocidad: 'precocidad alta',
        productividad: 'alta',
        adaptacion: 'excelente en suelos de buena fertilidad',
        resistencia_plagas: ['carbunclo', 'roya'],
    },
    {
        nombre: 'CP72-2086',
        precocidad: 'muy alta',
        productividad: 'alta',
        adaptacion: 'suelos de mediana fertilidad',
        resistencia_plagas: ['carbunclo'],
    },
    {
        nombre: 'RB72454',
        precocidad: 'media',
        productividad: 'muy alta',
        adaptacion: 'suelos de alta fertilidad',
        resistencia_plagas: ['roya', 'pokkah_boeng'],
    },
    {
        nombre: 'SP81-3250',
        precocidad: 'muy alta',
        productividad: 'alta',
        adaptacion: 'amplia adaptación',
        resistencia_plagas: ['carbunclo', 'roya'],
    },
];

// ==================== INTERVALOS DE ACTUALIZACIÓN PARA CAÑA ====================
export const INTERVALOS_CANA = {
    sensores_tiempo_real: 20000, // 20 segundos (más frecuente)
    predicciones: 600000, // 10 minutos
    analisis_plagas: 300000, // 5 minutos
    analisis_suelo: 604800000, // 1 semana
    calidad_brix_pol: 86400000, // 1 día
};

// ==================== FACTORES DE CÁLCULO PARA RENDIMIENTO ====================
export const FACTORES_RENDIMIENTO_CANA = {
    temperatura_factor: 0.8, // Peso en predicción
    humedad_factor: 1.2, // Peso en predicción
    nutrientes_factor: 0.9,
    plagas_factor: 1.1,
    luz_factor: 0.7,
    base_rendimiento: 65, // Toneladas/hectárea como base
};
