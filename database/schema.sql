-- ============================================
-- INGENIERÍA DEL AHORRO - Base de Datos
-- ============================================

CREATE DATABASE IF NOT EXISTS ingenieria_ahorro
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE ingenieria_ahorro;

-- Tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  foto_perfil VARCHAR(255),
  nivel_ahorro VARCHAR(50) DEFAULT 'Principiante',
  fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
  activo BOOLEAN DEFAULT TRUE,
  dark_mode BOOLEAN DEFAULT FALSE
);

-- Tabla ingresos
CREATE TABLE IF NOT EXISTS ingresos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  categoria VARCHAR(80) NOT NULL,
  valor DECIMAL(15,2) NOT NULL,
  fecha DATE NOT NULL,
  descripcion TEXT,
  metodo_pago VARCHAR(80),
  mes INT,
  anio INT,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla gastos_fijos
CREATE TABLE IF NOT EXISTS gastos_fijos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  valor DECIMAL(15,2) NOT NULL,
  fecha_pago DATE,
  dia_pago INT,
  estado VARCHAR(30) DEFAULT 'Pendiente',
  prioridad VARCHAR(20) DEFAULT 'Media',
  categoria VARCHAR(80),
  descripcion TEXT,
  mes INT,
  anio INT,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla gastos_hormiga
CREATE TABLE IF NOT EXISTS gastos_hormiga (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  categoria VARCHAR(80) NOT NULL,
  valor DECIMAL(15,2) NOT NULL,
  fecha DATE NOT NULL,
  descripcion TEXT,
  mes INT,
  anio INT,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla deudas
CREATE TABLE IF NOT EXISTS deudas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  acreedor VARCHAR(150) NOT NULL,
  valor_total DECIMAL(15,2) NOT NULL,
  valor_pagado DECIMAL(15,2) DEFAULT 0,
  cuota_mensual DECIMAL(15,2),
  interes DECIMAL(5,2) DEFAULT 0,
  fecha_inicio DATE,
  fecha_limite DATE,
  estado VARCHAR(30) DEFAULT 'Activa',
  tipo VARCHAR(50),
  descripcion TEXT,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla presupuestos
CREATE TABLE IF NOT EXISTS presupuestos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  categoria VARCHAR(80) NOT NULL,
  valor_maximo DECIMAL(15,2) NOT NULL,
  valor_gastado DECIMAL(15,2) DEFAULT 0,
  mes INT NOT NULL,
  anio INT NOT NULL,
  descripcion TEXT,
  color VARCHAR(20) DEFAULT '#1B4F72',
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla metas_ahorro
CREATE TABLE IF NOT EXISTS metas_ahorro (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  valor_meta DECIMAL(15,2) NOT NULL,
  valor_ahorrado DECIMAL(15,2) DEFAULT 0,
  fecha_objetivo DATE,
  estado VARCHAR(30) DEFAULT 'En progreso',
  icono VARCHAR(10) DEFAULT '🎯',
  color VARCHAR(20) DEFAULT '#27AE60',
  descripcion TEXT,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Índices para mejor rendimiento
CREATE INDEX idx_ingresos_usuario_mes ON ingresos(usuario_id, mes, anio);
CREATE INDEX idx_gastos_fijos_usuario_mes ON gastos_fijos(usuario_id, mes, anio);
CREATE INDEX idx_gastos_hormiga_usuario_mes ON gastos_hormiga(usuario_id, mes, anio);
CREATE INDEX idx_deudas_usuario ON deudas(usuario_id);
CREATE INDEX idx_presupuestos_usuario_mes ON presupuestos(usuario_id, mes, anio);
CREATE INDEX idx_metas_usuario ON metas_ahorro(usuario_id);

-- Usuario de prueba (password: 123456)
INSERT INTO usuarios (nombre, correo, password, nivel_ahorro) VALUES
('Andrés Demo', 'demo@ahorro.com', '$2a$10$N.zmdr9zkoa05OYx9Z6L5uBbFMFMFMFMFMFMFMFMFMFMFMFMFMFMF', 'Intermedio')
ON DUPLICATE KEY UPDATE id=id;
