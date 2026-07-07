CREATE DATABASE IF NOT EXISTS coloniagatos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE coloniagatos;

CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255),
  password VARCHAR(255),
  gmail VARCHAR(255),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS administrador (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  usuario_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_administrador_usuario (usuario_id),
  CONSTRAINT fk_administrador_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS veterinario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  numeroTelefono VARCHAR(255),
  dni VARCHAR(255),
  usuario_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_veterinario_usuario (usuario_id),
  CONSTRAINT fk_veterinario_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS voluntario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  dni VARCHAR(255),
  reputacion DOUBLE,
  username VARCHAR(255),
  password VARCHAR(255),
  usuario_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_voluntario_usuario (usuario_id),
  CONSTRAINT fk_voluntario_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS familia (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  numeroTelefono VARCHAR(255),
  cantIntegrantes INT,
  direccion VARCHAR(255),
  responsable VARCHAR(255),
  reputacion DOUBLE,
  username VARCHAR(255),
  password VARCHAR(255),
  usuario_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_familia_usuario (usuario_id),
  CONSTRAINT fk_familia_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS colonia (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  latMin DOUBLE,
  latMax DOUBLE,
  lonMin DOUBLE,
  lonMax DOUBLE,
  descripcion VARCHAR(255),
  administrador_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_colonia_administrador (administrador_id),
  CONSTRAINT fk_colonia_administrador
    FOREIGN KEY (administrador_id) REFERENCES administrador (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS gato (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  color VARCHAR(255),
  caracteristicas VARCHAR(255),
  foto VARCHAR(255),
  estadoSalud VARCHAR(255),
  foto_path VARCHAR(255),
  qr VARCHAR(255),
  aptoAdopcion BIT,
  fechaRegistro DATE,
  colonia_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_gato_colonia (colonia_id),
  CONSTRAINT fk_gato_colonia
    FOREIGN KEY (colonia_id) REFERENCES colonia (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS hogar_transito (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  disponible BIT,
  direccion VARCHAR(255),
  caracteristica VARCHAR(255),
  voluntario_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_hogar_transito_voluntario (voluntario_id),
  CONSTRAINT fk_hogar_transito_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS historial_medico (
  id BIGINT NOT NULL AUTO_INCREMENT,
  notas VARCHAR(255),
  gato_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_historial_medico_gato (gato_id),
  CONSTRAINT fk_historial_medico_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS asignacion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha DATE,
  hora TIME,
  voluntario_id BIGINT,
  familia_id BIGINT,
  gato_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_asignacion_voluntario (voluntario_id),
  INDEX idx_asignacion_familia (familia_id),
  INDEX idx_asignacion_gato (gato_id),
  CONSTRAINT fk_asignacion_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id),
  CONSTRAINT fk_asignacion_familia
    FOREIGN KEY (familia_id) REFERENCES familia (id),
  CONSTRAINT fk_asignacion_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS postulacion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha DATE,
  estado VARCHAR(255),
  familia_id BIGINT,
  gato_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_postulacion_familia (familia_id),
  INDEX idx_postulacion_gato (gato_id),
  CONSTRAINT fk_postulacion_familia
    FOREIGN KEY (familia_id) REFERENCES familia (id),
  CONSTRAINT fk_postulacion_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS visita (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha DATE,
  hora TIME,
  descripcion VARCHAR(255),
  voluntario_id BIGINT,
  familia_id BIGINT,
  gato_id BIGINT,
  hogartransito_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_visita_voluntario (voluntario_id),
  INDEX idx_visita_familia (familia_id),
  INDEX idx_visita_gato (gato_id),
  INDEX idx_visita_hogartransito (hogartransito_id),
  CONSTRAINT fk_visita_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id),
  CONSTRAINT fk_visita_familia
    FOREIGN KEY (familia_id) REFERENCES familia (id),
  CONSTRAINT fk_visita_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id),
  CONSTRAINT fk_visita_hogartransito
    FOREIGN KEY (hogartransito_id) REFERENCES hogar_transito (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS punto_avistamiento (
  id BIGINT NOT NULL AUTO_INCREMENT,
  coordena VARCHAR(255),
  fecha DATE,
  hora TIME,
  estadosalud VARCHAR(255),
  voluntario_id BIGINT,
  gato_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_punto_avistamiento_voluntario (voluntario_id),
  INDEX idx_punto_avistamiento_gato (gato_id),
  CONSTRAINT fk_punto_avistamiento_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id),
  CONSTRAINT fk_punto_avistamiento_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS transporte_hogar_transito (
  id BIGINT NOT NULL AUTO_INCREMENT,
  horaEntrega TIME,
  aclaracion VARCHAR(255),
  ubicacion VARCHAR(255),
  fecha DATE,
  hora TIME,
  voluntario_id BIGINT,
  gato_id BIGINT,
  hogartransito_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_transporte_voluntario (voluntario_id),
  INDEX idx_transporte_gato (gato_id),
  INDEX idx_transporte_hogartransito (hogartransito_id),
  CONSTRAINT fk_transporte_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id),
  CONSTRAINT fk_transporte_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id),
  CONSTRAINT fk_transporte_hogartransito
    FOREIGN KEY (hogartransito_id) REFERENCES hogar_transito (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS captura_castracion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombreVeterinario VARCHAR(255),
  numeroVeterinario VARCHAR(255),
  documento VARCHAR(255),
  ubicacion VARCHAR(255),
  fecha DATE,
  hora TIME,
  voluntario_id BIGINT,
  gato_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_captura_castracion_voluntario (voluntario_id),
  INDEX idx_captura_castracion_gato (gato_id),
  CONSTRAINT fk_captura_castracion_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id),
  CONSTRAINT fk_captura_castracion_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS control_veterinario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombreVeterinario VARCHAR(255),
  numeroVeterinario VARCHAR(255),
  tipo VARCHAR(255),
  documento VARCHAR(255),
  ubicacion VARCHAR(255),
  fecha DATE,
  hora TIME,
  voluntario_id BIGINT,
  gato_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_control_veterinario_voluntario (voluntario_id),
  INDEX idx_control_veterinario_gato (gato_id),
  CONSTRAINT fk_control_veterinario_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id),
  CONSTRAINT fk_control_veterinario_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS alimentacion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  tipo VARCHAR(255),
  cantidad VARCHAR(255),
  ubicacion VARCHAR(255),
  fecha DATE,
  hora TIME,
  voluntario_id BIGINT,
  gato_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_alimentacion_voluntario (voluntario_id),
  INDEX idx_alimentacion_gato (gato_id),
  CONSTRAINT fk_alimentacion_voluntario
    FOREIGN KEY (voluntario_id) REFERENCES voluntario (id),
  CONSTRAINT fk_alimentacion_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS certificado_adopcion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  comentario VARCHAR(255),
  fecha DATE,
  gato_id BIGINT,
  veterinario_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_certificado_adopcion_gato (gato_id),
  INDEX idx_certificado_adopcion_veterinario (veterinario_id),
  CONSTRAINT fk_certificado_adopcion_gato
    FOREIGN KEY (gato_id) REFERENCES gato (id),
  CONSTRAINT fk_certificado_adopcion_veterinario
    FOREIGN KEY (veterinario_id) REFERENCES veterinario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS diagnostico (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  fecha DATE,
  historialmedico_id BIGINT,
  veterinario_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_diagnostico_historialmedico (historialmedico_id),
  INDEX idx_diagnostico_veterinario (veterinario_id),
  CONSTRAINT fk_diagnostico_historialmedico
    FOREIGN KEY (historialmedico_id) REFERENCES historial_medico (id),
  CONSTRAINT fk_diagnostico_veterinario
    FOREIGN KEY (veterinario_id) REFERENCES veterinario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS estudio (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  descripcion VARCHAR(255),
  documento VARCHAR(255),
  diagnostico_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_estudio_diagnostico (diagnostico_id),
  CONSTRAINT fk_estudio_diagnostico
    FOREIGN KEY (diagnostico_id) REFERENCES diagnostico (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tratamiento (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  fecha DATE,
  descripcion VARCHAR(255),
  diagnostico_id BIGINT,
  PRIMARY KEY (id),
  INDEX idx_tratamiento_diagnostico (diagnostico_id),
  CONSTRAINT fk_tratamiento_diagnostico
    FOREIGN KEY (diagnostico_id) REFERENCES diagnostico (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
