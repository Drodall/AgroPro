import React, { useState } from 'react';
import { DashboardAgricola } from './dashboard';
import { servicioAutenticacion } from './servicios-api';

// ==================== COMPONENTE LOGIN ====================
const PantallaLogin = ({ onLogin }: { onLogin: () => void }) => {
    const [email, setEmail] = useState('');
    const [contrasena, setContrasena] = useState('');
    const [cargando, setCargando] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            setCargando(true);
            setError(null);

            const respuesta = await servicioAutenticacion.login(email, contrasena);

            if (respuesta.exito) {
                // ✅ Guardar token en localStorage
                localStorage.setItem('auth_token', respuesta.datos.token);
                
                // ✅ Guardar usuario si existe
                if (respuesta.datos.usuario) {
                    localStorage.setItem('usuario', JSON.stringify(respuesta.datos.usuario));
                }
                
                onLogin();
            } else {
                setError(respuesta.mensaje || 'Error al iniciar sesión');
            }
        } catch (err) {
            setError('Error al iniciar sesión. Intenta de nuevo.');
            console.error(err);
        } finally {
            setCargando(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-green-600 to-blue-600 flex items-center justify-center p-4">
            <div className="bg-white rounded-xl shadow-2xl p-8 max-w-md w-full">
                <h1 className="text-3xl font-bold text-center text-green-800 mb-8">
                    🌾 AgroPRO
                </h1>

                {error && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                        {error}
                    </div>
                )}

                <form onSubmit={handleLogin}>
                    <div className="mb-4">
                        <label className="block text-gray-700 text-sm font-semibold mb-2">
                            Email
                        </label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-green-500"
                            required
                        />
                    </div>

                    <div className="mb-6">
                        <label className="block text-gray-700 text-sm font-semibold mb-2">
                            Contraseña
                        </label>
                        <input
                            type="password"
                            value={contrasena}
                            onChange={(e) => setContrasena(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-green-500"
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={cargando}
                        className="w-full bg-gradient-to-r from-green-600 to-blue-600 text-white font-semibold py-2 rounded-lg hover:opacity-90 transition disabled:opacity-50"
                    >
                        {cargando ? 'Iniciando sesión...' : 'Iniciar Sesión'}
                    </button>
                </form>

                <p className="text-center text-gray-600 text-sm mt-4">
                    ¿No tienes cuenta? <button 
                        className="text-green-600 hover:underline bg-none border-none cursor-pointer"
                        onClick={(e) => {
                            e.preventDefault();
                            alert('Función de registro próximamente disponible');
                        }}
                    >
                        Regístrate aquí
                    </button>
                </p>
            </div>
        </div>
    );
};

// ==================== COMPONENTE PRINCIPAL APP ====================
export const App = () => {
    const [autenticado, setAutenticado] = useState(() => {
        return !!localStorage.getItem('auth_token');
    });

    const handleLogout = async () => {
        try {
            await servicioAutenticacion.logout();
        } catch (err) {
            console.error('Error al cerrar sesión:', err);
        } finally {
            // ✅ Limpiar localStorage
            localStorage.removeItem('auth_token');
            localStorage.removeItem('usuario');
            setAutenticado(false);
        }
    };

    if (!autenticado) {
        return <PantallaLogin onLogin={() => setAutenticado(true)} />;
    }

    return (
        <div>
            <header className="bg-white shadow-sm sticky top-0 z-50">
                <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
                    <div className="flex items-center gap-2">
                        <span className="text-2xl">🌾</span>
                        <h1 className="text-xl font-bold text-green-800">AgroPRO</h1>
                    </div>
                    <button
                        onClick={handleLogout}
                        className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition"
                    >
                        Cerrar Sesión
                    </button>
                </div>
            </header>
            <main>
                <DashboardAgricola />
            </main>
        </div>
    );
};

export default App;
