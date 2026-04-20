import React, { useState, useEffect, useCallback } from 'react';
import { servicioIA, servicioCampos } from './servicios-api';
import type { 
    Campo, 
    Respuesta_Predicciones, 
    Recomendacion 
} from './servicios-api';

// ==================== COMPONENTE DASHBOARD PRINCIPAL ====================
export const DashboardAgricola = () => {
    const [campos, setCampos] = useState<Campo[]>([]);
    const [campoSeleccionado, setCampoSeleccionado] = useState<string | null>(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        cargarCampos();
    }, []);

    const cargarCampos = async () => {
        try {
            setCargando(true);
            const respuesta = await servicioCampos.obtenerTodosCampos();
            if (respuesta.exito) {
                setCampos(respuesta.datos);
                if (respuesta.datos.length > 0) {
                    // ✅ ERROR 1 CORREGIDO: Usar 'id' en lugar de 'idCampo'
                    setCampoSeleccionado(respuesta.datos[0].id);
                }
            } else {
                setError(respuesta.mensaje);
            }
        } catch (err) {
            setError('Error al cargar campos');
            console.error(err);
        } finally {
            setCargando(false);
        }
    };

    if (cargando) return <div className="p-4">Cargando...</div>;

    return (
        <div className="min-h-screen bg-gradient-to-br from-green-50 to-blue-50 p-6">
            <h1 className="text-4xl font-bold text-green-800 mb-8">Sistema Agrícola con IA</h1>

            {error && <div className="bg-red-200 p-4 rounded mb-4 text-red-800">{error}</div>}

            <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
                {/* Sidebar - Campos */}
                <div className="lg:col-span-1 bg-white rounded-lg shadow-lg p-4">
                    <h2 className="text-xl font-semibold mb-4 text-gray-800">Mis Campos</h2>
                    {campos.map((campo) => (
                        <button
                            key={campo.id}
                            onClick={() => setCampoSeleccionado(campo.id)}
                            className={`w-full p-3 mb-2 rounded transition ${
                                campoSeleccionado === campo.id
                                    ? 'bg-green-600 text-white'
                                    : 'bg-gray-100 text-gray-800 hover:bg-gray-200'
                            }`}
                        >
                            {campo.nombre}
                        </button>
                    ))}
                </div>

                {/* Contenido Principal */}
                <div className="lg:col-span-3">
                    {campoSeleccionado && (
                        <>
                            <TarjetaPrediccionesIA idCampo={campoSeleccionado} />
                            <TarjetaSensores idCampo={campoSeleccionado} />
                            <TarjetaRecomendaciones idCampo={campoSeleccionado} />
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

// ==================== COMPONENTE PREDICCIONES IA ====================
const TarjetaPrediccionesIA = ({ idCampo }: { idCampo: string }) => {
    const [predicciones, setPredicciones] = useState<Respuesta_Predicciones | null>(null);
    const [cargando, setCargando] = useState(true);

    const cargarPredicciones = useCallback(async () => {
        try {
            setCargando(true);
            const respuesta = await servicioIA.obtenerPredicciones(idCampo);
            if (respuesta.exito) {
                setPredicciones(respuesta.datos);
            }
        } catch (err) {
            console.error('Error al cargar predicciones:', err);
        } finally {
            setCargando(false);
        }
    }, [idCampo]);

    useEffect(() => {
        cargarPredicciones();
    }, [cargarPredicciones, idCampo]);

    if (cargando) return <div className="p-4">Cargando predicciones...</div>;

    return (
        <div className="bg-white rounded-lg shadow-lg p-6 mb-6">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">🤖 Análisis IA</h2>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {/* Predicción Cosecha */}
                {predicciones?.cosecha && (
                    <div className="bg-gradient-to-br from-yellow-100 to-orange-100 p-4 rounded-lg border-l-4 border-orange-500">
                        <h3 className="font-semibold text-lg mb-2 text-gray-800">🌾 Predicción Cosecha</h3>
                        <p className="text-sm text-gray-600">Rendimiento esperado:</p>
                        <p className="text-3xl font-bold text-orange-600">
                            {/* ✅ ERROR 2 CORREGIDO: Cambiar 'rendimiento_esperado' por 'rendimiento_estimado' */}
                            {predicciones.cosecha.rendimiento_estimado} kg/ha
                        </p>
                        <p className="text-xs text-gray-500 mt-2">
                            {/* ✅ ERROR 3 CORREGIDO: Cambiar 'confianza' por 'confianza' (este es correcto) */}
                            Confianza: {predicciones.cosecha.confianza}%
                        </p>
                        <p className="text-xs text-gray-500">
                            {/* ✅ ERROR 4 CORREGIDO: Cambiar 'fecha_cosecha_estimada' por 'fecha_estimada_cosecha' */}
                            Cosecha: {new Date(predicciones.cosecha.fecha_estimada_cosecha).toLocaleDateString()}
                        </p>
                    </div>
                )}

                {/* Detección Plagas */}
                {predicciones?.plagas && (
                    <div className={`p-4 rounded-lg border-l-4 ${
                        predicciones.plagas.severidad === 'alta'
                            ? 'bg-red-100 border-red-500'
                            : predicciones.plagas.severidad === 'media'
                            ? 'bg-yellow-100 border-yellow-500'
                            : 'bg-green-100 border-green-500'
                    }`}>
                        <h3 className="font-semibold text-lg mb-2 text-gray-800">🐛 Detección Plagas</h3>
                        <p className="text-sm text-gray-600">Probabilidad:</p>
                        <p className="text-3xl font-bold text-red-600">
                            {predicciones.plagas.probabilidad_plaga}%
                        </p>
                        <p className="text-xs text-gray-600 mt-2">
                            {predicciones.plagas.tipo_plaga_detectada}
                        </p>
                        <p className="text-xs font-semibold mt-2 text-gray-700">
                            Severidad: <span className="uppercase">{predicciones.plagas.severidad}</span>
                        </p>
                    </div>
                )}

                {/* Optimización Riego */}
                {predicciones?.riego && (
                    <div className="bg-gradient-to-br from-blue-100 to-cyan-100 p-4 rounded-lg border-l-4 border-blue-500">
                        <h3 className="font-semibold text-lg mb-2 text-gray-800">💧 Optimización Riego</h3>
                        <p className="text-sm text-gray-600">Próximo riego:</p>
                        <p className="text-lg font-bold text-blue-600">
                            {new Date(predicciones.riego.proxima_fecha_riego).toLocaleDateString()}
                        </p>
                        <p className="text-xs text-gray-600 mt-2">
                            Cantidad: {predicciones.riego.cantidad_litros} L
                        </p>
                        <p className="text-xs text-gray-600">
                            Duración: {predicciones.riego.duracion_minutos} min
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
};

// ==================== COMPONENTE SENSORES ====================
const TarjetaSensores = ({ idCampo }: { idCampo: string }) => {
    const [campo, setCampo] = useState<Campo | null>(null);
    const [cargando, setCargando] = useState(true);

    const cargarDatosCampo = useCallback(async () => {
        try {
            setCargando(true);
            const respuesta = await servicioCampos.obtenerCampo(idCampo);
            if (respuesta.exito) {
                setCampo(respuesta.datos);
            }
        } catch (err) {
            console.error('Error al cargar datos del campo:', err);
        } finally {
            setCargando(false);
        }
    }, [idCampo]);

    useEffect(() => {
        cargarDatosCampo();
        const intervalo = setInterval(cargarDatosCampo, 30000); // Actualizar cada 30 segundos
        return () => clearInterval(intervalo);
    }, [cargarDatosCampo]);

    if (cargando || !campo) return <div className="p-4">Cargando sensores...</div>;

    return (
        <div className="bg-white rounded-lg shadow-lg p-6 mb-6">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">📊 Datos de Sensores</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                {/* ✅ ERROR 5 CORREGIDO: Agregar condicional para verificar que sensores existe */}
                {campo.sensores && campo.sensores.map((sensor) => (
                    <div key={sensor.sensorId} className="bg-gradient-to-br from-purple-50 to-pink-50 p-4 rounded-lg border border-purple-200">
                        <p className="text-xs text-gray-500 mb-2">📍 {sensor.sensorId}</p>
                        
                        <div className="mb-3">
                            <p className="text-xs text-gray-600">Temperatura</p>
                            <p className="text-2xl font-bold text-red-600">{sensor.temperatura}°C</p>
                        </div>

                        <div className="mb-3">
                            <p className="text-xs text-gray-600">Humedad Aire</p>
                            <p className="text-2xl font-bold text-blue-600">{sensor.humedad}%</p>
                        </div>

                        <div className="mb-3">
                            <p className="text-xs text-gray-600">Humedad Suelo</p>
                            <p className="text-2xl font-bold text-green-600">{sensor.humedad_suelo}%</p>
                        </div>

                        <p className="text-xs text-gray-400 mt-3">
                            {new Date(sensor.fecha).toLocaleTimeString()}
                        </p>
                        
                        <div className="mt-2 inline-block px-2 py-1 rounded text-xs font-semibold"
                             style={{backgroundColor: sensor.estado === 'activo' ? '#dcfce7' : '#fee2e2',
                                    color: sensor.estado === 'activo' ? '#166534' : '#991b1b'}}>
                            {sensor.estado}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

// ==================== COMPONENTE RECOMENDACIONES ====================
const TarjetaRecomendaciones = ({ idCampo }: { idCampo: string }) => {
    const [recomendaciones, setRecomendaciones] = useState<Recomendacion[]>([]);
    const [cargando, setCargando] = useState(true);

    const cargarRecomendaciones = useCallback(async () => {
        try {
            setCargando(true);
            const respuesta = await servicioIA.obtenerRecomendaciones(idCampo);
            if (respuesta.exito) {
                setRecomendaciones(respuesta.datos);
            }
        } catch (err) {
            console.error('Error al cargar recomendaciones:', err);
        } finally {
            setCargando(false);
        }
    }, [idCampo]);

    useEffect(() => {
        cargarRecomendaciones();
    }, [cargarRecomendaciones]);

    if (cargando) return <div className="p-4">Cargando recomendaciones...</div>;

    return (
        <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">💡 Recomendaciones</h2>

            {recomendaciones.length === 0 ? (
                <p className="text-gray-500">No hay recomendaciones en este momento</p>
            ) : (
                <div className="space-y-3">
                    {recomendaciones.map((rec) => (
                        <div key={rec.id} className={`p-4 rounded-lg border-l-4 ${
                            rec.urgencia === 'critica' ? 'bg-red-50 border-red-500' :
                            rec.urgencia === 'alta' ? 'bg-orange-50 border-orange-500' :
                            rec.urgencia === 'media' ? 'bg-yellow-50 border-yellow-500' :
                            'bg-green-50 border-green-500'
                        }`}>
                            <div className="flex justify-between items-start mb-2">
                                <h3 className="font-semibold text-gray-800">{rec.titulo}</h3>
                                <span className={`px-2 py-1 rounded text-xs font-semibold uppercase ${
                                    rec.urgencia === 'critica' ? 'bg-red-200 text-red-800' :
                                    rec.urgencia === 'alta' ? 'bg-orange-200 text-orange-800' :
                                    rec.urgencia === 'media' ? 'bg-yellow-200 text-yellow-800' :
                                    'bg-green-200 text-green-800'
                                }`}>
                                    {rec.urgencia}
                                </span>
                            </div>
                            <p className="text-sm text-gray-700 mb-2">{rec.descripcion}</p>
                            <ul className="text-xs text-gray-600 space-y-1">
                                {rec.acciones_sugeridas.map((accion, idx) => (
                                    <li key={idx} className="ml-4">✓ {accion}</li>
                                ))}
                            </ul>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};
