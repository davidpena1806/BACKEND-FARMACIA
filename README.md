## Requisitos previos

- **Java 17+** (JDK) y **Maven 3.9+** (o usar el Maven integrado de IntelliJ IDEA / Eclipse / VS Code)
- **PostgreSQL** (con Docker o instalado)

### 1. Base de datos

Con Docker Compose (crea el contenedor y la base `farmacia_db`):

```bash
docker compose up -d
```

Si ya tienes un contenedor de PostgreSQL corriendo en el puerto 5432, solo crea la base:

```bash
docker exec -it <nombre-del-contenedor> psql -U postgres -c "CREATE DATABASE farmacia_db;"
```

Si tu usuario/contraseña son distintos, edita `src/main/resources/application.yml`.

### 2. Ejecutar la API

```bash
mvn spring-boot:run
```

- Swagger UI: **http://localhost:8080/swagger-ui.html** (botón _Authorize_ para pegar el token JWT)

### 3. Usuario inicial

Al arrancar por primera vez se crean los roles `ADMIN` y `VENDEDOR` y este usuario
(su contraseña se guarda cifrada con BCrypt):

| Correo               | Contraseña  | Rol   |
| -------------------- | ----------- | ----- |
| `admin@farmacia.com` | `Admin123*` | ADMIN |

## Probar el login

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{ "correo": "admin@farmacia.com", "password": "Admin123*" }
```

Ejemplo de un producto, el cuál se relaciona con categorias

```json
{
  "codigo": "PARA500",
  "nombre": "Paracetamol 500mg",
  "unidadMedida": "Tableta",
  "precioVenta": 0.5,
  "precioCompraReferencial": 0.3,
  "stock": 0,
  "stockMinimo": 50,
  "requiereReceta": false,
  "activo": true,
  "categoriaId": 1
}
```

## Notas

- Las relaciones se escriben solo por **id** (`categoriaId`, `rolId`...). Los objetos anidados
  (`categoria`, `rol`...) son de solo lectura, por lo que un cliente no puede crear registros
  relacionados por accidente.
- Reglas de negocio: no se vende sin stock suficiente, una caja cerrada no admite movimientos y
  un usuario no puede tener dos cajas abiertas.
- Mejoras posibles: pruebas automatizadas, descuento de stock por lote (FEFO), migraciones con Flyway,
  paginación de listados y reportes.
