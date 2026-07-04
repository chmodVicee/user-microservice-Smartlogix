# User Microservice — SmartLogix

Microservicio encargado del registro, autenticación y gestión de usuarios. Emite los **tokens JWT** que utilizan el resto de los microservicios para validar identidad y roles.

## Datos técnicos

| Campo | Valor |
|---|---|
| Puerto | `8083` |
| Base de datos | `users_db` (MySQL) |
| Autenticación | JWT (Bearer Token) |
| Roles disponibles | `USER`, `ADMIN` |

## Endpoints

### Públicos

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/register` | Registrar nuevo usuario |
| POST | `/api/auth/login` | Iniciar sesión, retorna JWT |

### Protegidos (requieren JWT)

| Método | Ruta | Rol mínimo | Descripción |
|---|---|---|---|
| GET | `/api/users/profile` | USER | Perfil del usuario autenticado |
| GET | `/api/users` | ADMIN | Lista de todos los usuarios |

---

## Pruebas en Postman

### 1. Registrar usuario

```
POST http://localhost:8083/api/auth/register
Content-Type: application/json

{
  "username": "juan",
  "email": "juan@example.com",
  "password": "123456"
}
```

**Respuesta esperada (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "juan",
  "role": "USER"
}
```

---

### 2. Iniciar sesión

```
POST http://localhost:8083/api/auth/login
Content-Type: application/json

{
  "username": "juan",
  "password": "123456"
}
```

> Guarda el `token` de la respuesta. Se usará como `Bearer Token` en todas las siguientes peticiones.

---

### 3. Ver perfil propio

```
GET http://localhost:8083/api/users/profile
Authorization: Bearer <token>
```

**Respuesta esperada (200):**
```json
{
  "id": 1,
  "username": "juan",
  "email": "juan@example.com",
  "role": "USER"
}
```

---

### 4. Listar todos los usuarios (solo ADMIN)

```
GET http://localhost:8083/api/users
Authorization: Bearer <token-de-admin>
```

> Si el token no pertenece a un ADMIN, retorna `403 Forbidden`.

---

## Cómo levantar

```bash
./mvnw spring-boot:run
```

Requiere MySQL corriendo en `localhost:3306` con usuario `root` / contraseña `root`.
