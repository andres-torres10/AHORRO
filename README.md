# 💰 Ingeniería del Ahorro
### Sistema de Administración Financiera Personal

---

## 🚀 Cómo ejecutar el proyecto

### Requisitos previos
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Navegador moderno (Chrome, Firefox, Edge)

---

## 📦 Paso 1 — Base de datos

1. Abre MySQL Workbench o tu cliente favorito
2. Ejecuta el script:
```sql
-- Ejecutar el archivo:
database/schema.sql
```
3. Verifica que se creó la base de datos `ingenieria_ahorro`

---

## ⚙️ Paso 2 — Configurar credenciales

Edita el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.username=TU_USUARIO_MYSQL
spring.datasource.password=TU_PASSWORD_MYSQL
```

---

## ▶️ Paso 3 — Ejecutar el backend

```bash
# Desde la carpeta raíz del proyecto
mvn spring-boot:run
```

El servidor arranca en: **http://localhost:8080/api**

---

## 🌐 Paso 4 — Abrir el frontend

Abre directamente en el navegador:
```
frontend/index.html
```

O usa Live Server en VS Code para mejor experiencia.

---

## 📁 Estructura del proyecto

```
AHORRO/
├── src/
│   └── main/
│       ├── java/com/ingenieriadelahorro/
│       │   ├── controller/          # Controladores REST
│       │   ├── service/             # Lógica de negocio
│       │   ├── repository/          # Acceso a datos (JPA)
│       │   ├── model/               # Entidades JPA
│       │   ├── dto/                 # Objetos de transferencia
│       │   └── security/            # JWT + Spring Security
│       └── resources/
│           └── application.properties
├── frontend/
│   ├── index.html                   # Login / Registro
│   ├── dashboard.html               # Dashboard principal
│   ├── ingresos.html                # Gestión de ingresos
│   ├── gastos-fijos.html            # Gastos fijos
│   ├── gastos-hormiga.html          # Gastos hormiga
│   ├── deudas.html                  # Gestión de deudas
│   ├── presupuesto.html             # Presupuestos
│   ├── metas.html                   # Metas de ahorro
│   ├── resumen.html                 # Resumen mensual
│   ├── graficas.html                # Gráficas Chart.js
│   ├── aprende.html                 # Educación financiera
│   ├── reportes.html                # Exportar PDF/Excel
│   ├── perfil.html                  # Mi perfil
│   ├── configuracion.html           # Configuración
│   ├── css/
│   │   ├── variables.css            # Variables CSS
│   │   ├── main.css                 # Estilos globales
│   │   └── auth.css                 # Estilos de login
│   └── js/
│       ├── api.js                   # Cliente API + utilidades
│       └── layout.js                # Sidebar y topbar
├── database/
│   └── schema.sql                   # Script de base de datos
└── pom.xml                          # Dependencias Maven
```

---

## 🔌 API REST Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registro de usuario |
| POST | `/api/auth/login` | Inicio de sesión |
| GET | `/api/dashboard` | Dashboard con resumen |
| GET/POST/PUT/DELETE | `/api/ingresos` | CRUD Ingresos |
| GET/POST/PUT/DELETE | `/api/gastos-fijos` | CRUD Gastos Fijos |
| GET/POST/PUT/DELETE | `/api/gastos-hormiga` | CRUD Gastos Hormiga |
| GET/POST/PUT/DELETE | `/api/deudas` | CRUD Deudas |
| GET/POST/PUT/DELETE | `/api/presupuestos` | CRUD Presupuestos |
| GET/POST/PUT/DELETE | `/api/metas` | CRUD Metas de Ahorro |
| GET | `/api/reportes/pdf` | Exportar PDF |
| GET | `/api/reportes/excel` | Exportar Excel |
| GET/PUT | `/api/usuarios/perfil` | Perfil de usuario |

---

## 🎨 Paleta de colores

| Color | Hex | Uso |
|-------|-----|-----|
| Azul principal | `#1B4F72` | Sidebar, botones primarios |
| Azul secundario | `#21618C` | Hover, acentos |
| Verde | `#27AE60` | Ingresos, éxito |
| Rojo | `#E74C3C` | Gastos, peligro |
| Naranja | `#F39C12` | Advertencias |

---

## ✅ Funcionalidades implementadas

- ✅ Login / Registro con JWT
- ✅ Dashboard con semáforo financiero
- ✅ CRUD completo: Ingresos, Gastos Fijos, Gastos Hormiga, Deudas, Presupuestos, Metas
- ✅ Exportar PDF y Excel
- ✅ Gráficas con Chart.js
- ✅ Dark Mode / Light Mode
- ✅ Responsive (móvil, tablet, desktop)
- ✅ Sidebar colapsable
- ✅ Toast notifications
- ✅ Confirmación al eliminar
- ✅ Alertas automáticas de vencimiento
- ✅ Consejos financieros personalizados
- ✅ Centro de educación financiera
- ✅ Metas de ahorro con barras animadas
- ✅ Búsqueda y filtros en tablas
- ✅ Subida de foto de perfil

---

## 🔐 Seguridad

- Contraseñas hasheadas con BCrypt
- Autenticación JWT (24h de expiración)
- CORS configurado
- Validación de datos en backend
- Cada usuario solo accede a sus propios datos
