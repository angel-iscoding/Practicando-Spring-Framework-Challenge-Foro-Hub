# Foro Hub - API REST

## Descripción

Foro Hub es una API REST desarrollada con Spring Boot que simula un foro de discusión. Los usuarios pueden crear, listar, actualizar y eliminar tópicos de discusión. La API cuenta con un sistema de autenticación JWT para proteger los endpoints.

## Características

- ✅ Gestión completa de tópicos (CRUD)
- ✅ Autenticación JWT
- ✅ Validación de datos
- ✅ Base de datos MySQL
- ✅ Migraciones con Flyway
- ✅ Documentación de API

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **Flyway** (para migraciones)
- **JWT** (para autenticación)
- **Lombok**
- **Bean Validation**

## Configuración del Proyecto


### Configuración de Base de Datos

1. Crear una base de datos MySQL llamada `foro_hub`
2. Configurar las credenciales en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foro_hub
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### Ejecución

1. Clonar el repositorio
2. Configurar la base de datos
3. Ejecutar el proyecto:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints de la API

### Autenticación

#### POST /login
Autenticar usuario y obtener token JWT.

**Request:**
```json
{
    "login": "admin",
    "password": "123456"
}
```

**Response:**
```json
{
    "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
}
```

### Tópicos (Requieren autenticación)

Todos los siguientes endpoints requieren el header:
```
Authorization: Bearer {tu_token_jwt}
```

#### GET /topicos
Listar todos los tópicos.

#### POST /topicos
Crear un nuevo tópico.

**Request:**
```json
{
    "titulo": "Mi primer tópico",
    "mensaje": "Este es el contenido del tópico",
    "autor": "Nombre del autor",
    "curso": "Spring Boot"
}
```

#### GET /topicos/{id}
Obtener un tópico específico por ID.

#### PUT /topicos/{id}
Actualizar un tópico existente.

**Request:**
```json
{
    "titulo": "Título actualizado",
    "mensaje": "Mensaje actualizado"
}
```

#### DELETE /topicos/{id}
Eliminar un tópico.

## Seguridad

- La API utiliza autenticación JWT
- Los tokens tienen una expiración de 2 horas
- Todas las rutas excepto `/login` requieren autenticación
- Las contraseñas se almacenan hasheadas con BCrypt

## Usuario por Defecto

Para facilitar las pruebas, se crea automáticamente un usuario por defecto:

- **Login:** `admin`
- **Contraseña:** `123456`

## Estructura del Proyecto

```
src/main/java/com/alura/challenge/foro/
├── controller/
│   ├── AutenticacionController.java
│   └── TopicoController.java
├── security/
│   ├── AutenticacionService.java
│   ├── SecurityConfigurations.java
│   ├── SecurityFilter.java
│   └── TokenService.java
├── topico/
│   ├── Topico.java
│   ├── TopicoRepository.java
│   └── [DTOs relacionados]
├── usuario/
│   ├── Usuario.java
│   ├── UsuarioRepository.java
│   └── [DTOs relacionados]
└── PracticandoSpringFrameworkChallengeForoHubApplication.java
```

## Testing

Para probar la API, puedes usar herramientas como:

- **Postman**
- **Insomnia**
- **cURL**

### Ejemplo con cURL

1. Obtener token:
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","clave":"123456"}'
```

2. Crear tópico:
```bash
curl -X POST http://localhost:8080/topicos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {tu_token}" \
  -d '{
    "titulo": "Mi tópico",
    "mensaje": "Contenido del tópico",
    "autor": "Usuario",
    "curso": "Spring Boot"
  }'
```

