CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(300) NOT NULL,
    PRIMARY KEY (id)
);

-- Crear un usuario por defecto para pruebas
-- La contraseña es '123456' hasheada con BCrypt
INSERT INTO usuarios (login, clave) VALUES 
('admin', '$2a$10$Y50UaMFOxteibQEYLrwuAuSuZYsHq5qJBFUpJU.mGYGw3uHYYKpqy');
