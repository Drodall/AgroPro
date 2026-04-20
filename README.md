# 🌾 AgroPRO - Sistema Agrícola Inteligente

Sistema completo de gestión agrícola con inteligencia artificial, sensores IoT y análisis predictivo.

## 📋 Requisitos Previos

- **Node.js** v16+ ([descargar](https://nodejs.org/))
- **npm** o **yarn** (viene con Node.js)
- **Git** (opcional)

## 🚀 Instalación Rápida

### 1. Instalar Dependencias

```bash
npm install
```

### 2. Configurar Variables de Entorno

Copia el archivo `.env.example` a `.env`:

```bash
cp .env.example .env
```

Edita `.env` con tu configuración:
```
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENV=development
```

### 3. Iniciar el Servidor de Desarrollo

```bash
npm start
```

La aplicación se abrirá en `http://localhost:3000`

## 📦 Dependencias Principales

### Producción
- **react** (^18.2.0) - Librería UI
- **react-dom** (^18.2.0) - Renderización DOM
- **react-router-dom** (^6.15.0) - Enrutamiento
- **axios** (^1.6.0) - HTTP client
- **socket.io-client** (^4.5.0) - WebSocket tiempo real

### Desarrollo
- **typescript** (^5.0.0) - Tipado estático
- **tailwindcss** (^3.3.0) - Framework CSS
- **react-scripts** (5.0.1) - Build tools
- **eslint** (^8.50.0) - Linting

## 📂 Estructura del Proyecto

```
Sistema Agricola/
├── src/
│   ├── modulo.ts              # Tipos e interfaces
│   ├── servicios-api.ts       # Cliente API
│   ├── servicio-cana-azucar.ts # Servicio específico
│   ├── constantes-cana-azucar.ts # Constantes
│   ├── dashboard.tsx          # Dashboard componente
│   ├── app.tsx               # App principal
│   ├── hooks-utilidades.ts   # Hooks personalizados
│   ├── index.tsx             # Entry point
│   └── index.css             # Estilos globales
├── public/
├── package.json
├── tsconfig.json
├── tailwind.config.js
├── postcss.config.js
└── .env
```

## 🛠️ Scripts Disponibles

```bash
# Desarrollo
npm start              # Inicia servidor de desarrollo

# Build
npm run build          # Crea build optimizado

# Testing
npm test              # Ejecuta tests

# Linting
npm run lint          # Ejecuta ESLint
npm run type-check    # Verifica tipos TypeScript
```

## 🔌 Conexión con Backend

El frontend se conecta al backend Java en `http://localhost:8080/api`

### Endpoints esperados:

**Campos:**
- `GET /api/fields` - Obtener todos los campos
- `GET /api/fields/:id` - Obtener campo específico
- `POST /api/fields` - Crear nuevo campo
- `PUT /api/fields/:id` - Actualizar campo
- `DELETE /api/fields/:id` - Eliminar campo

**Sensores:**
- `GET /api/sensors/:id` - Obtener sensor
- `GET /api/fields/:id/sensors` - Sensores del campo
- `POST /api/sensors/data` - Enviar datos de sensor

**IA y Predicciones:**
- `GET /api/ai/predictions/:fieldId` - Predicciones
- `GET /api/ai/crop-prediction/:fieldId` - Predicción cosecha
- `GET /api/ai/plague-detection/:fieldId` - Detección plagas
- `POST /api/ai/plague-detection` - Analizar imagen
- `GET /api/ai/irrigation-optimization/:fieldId` - Optimizar riego
- `GET /ai/recommendations/:fieldId` - Recomendaciones

**Caña de Azúcar (específico):**
- `GET /api/cana/metricas/:fieldId` - Métricas caña
- `GET /api/cana/prediccion/:fieldId` - Predicción caña
- `GET /api/cana/plagas/:fieldId` - Plagas detectadas
- `GET /api/cana/analisis-suelo/:fieldId` - Análisis suelo

## 🔐 Autenticación

Los endpoints requieren token JWT en header:

```
Authorization: Bearer <token>
```

El token se obtiene mediante:
```
POST /api/auth/login
{
  "email": "usuario@example.com",
  "contraseña": "password"
}
```

## 🌡️ Umbrales para Caña de Azúcar

Consultados automáticamente desde `constantes-cana-azucar.ts`:

- **Temperatura:** 20-30°C óptimo
- **Humedad:** 60-80% relativa
- **Humedad Suelo:** 65-75% capacidad de campo
- **Brix:** 15-20 sólidos solubles
- **Pol:** 13-18 sacarosa
- **pH Suelo:** 5.5-7.5

## 🐛 Troubleshooting

### Error: Cannot find module '@'

Verifica que `tsconfig.json` tenga las rutas configuradas:
```json
"paths": {
  "@/*": ["src/*"]
}
```

### Error: Tailwind CSS no funciona

Asegúrate de tener instalado:
```bash
npm install -D tailwindcss postcss autoprefixer
```

### Error de conexión API

Verifica:
1. Backend está corriendo en `localhost:8080`
2. `.env` tiene URL correcta: `REACT_APP_API_URL=http://localhost:8080/api`
3. CORS está habilitado en backend

### Port 3000 en uso

```bash
# Linux/Mac
lsof -i :3000
kill -9 <PID>

# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

## 📱 Dispositivos Soportados

- Desktop (Chrome, Firefox, Safari, Edge)
- Tablet (iPad, Android)
- Mobile (iOS, Android)

## 🔒 Seguridad

- Tokens almacenados en `localStorage`
- HTTPS recomendado en producción
- CORS configurado en backend
- Validación en cliente y servidor

## 📊 Performance

- Code splitting automático
- Lazy loading de componentes
- Caché de datos (5 minutos)
- Compresión gzip

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Proyecto privado - Todos los derechos reservados

## 🆘 Soporte

Para problemas o dudas, contacta al equipo de desarrollo.

---

**Última actualización:** Abril 2026
