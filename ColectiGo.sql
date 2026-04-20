-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema colectigo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema colectigo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `colectigo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `colectigo` ;

-- -----------------------------------------------------
-- Table `colectigo`.`administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colectigo`.`administradores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario` VARCHAR(20) NOT NULL,
  `password_hash` VARCHAR(30) NOT NULL,
  `ultimo_acesso` DATETIME NULL DEFAULT NULL,
  `activo` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `usuario` (`usuario` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `colectigo`.`lineas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colectigo`.`lineas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(10) NOT NULL,
  `tarifa` INT NULL DEFAULT NULL,
  `color_hex` VARCHAR(7) NULL DEFAULT '#FFD700',
  `frecuencia_estimada` VARCHAR(50) NULL DEFAULT NULL,
  `horario_funcionamiento` VARCHAR(100) NULL DEFAULT NULL,
  `activo` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `colectigo`.`recorridos_texto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colectigo`.`recorridos_texto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `linea_id` INT NOT NULL,
  `sentido` ENUM('Ida', 'Vuelta', 'Circular') NULL DEFAULT 'Ida',
  `descripcion_calles` TEXT NOT NULL,
  `hitos_principales` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `linea_id` (`linea_id` ASC) VISIBLE,
  CONSTRAINT `recorridos_texto_ibfk_1`
    FOREIGN KEY (`linea_id`)
    REFERENCES `colectigo`.`lineas` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `colectigo`.`trazados_geo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colectigo`.`trazados_geo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `linea_id` INT NOT NULL,
  `sentido` ENUM('Ida', 'Vuelta', 'Circular') NULL DEFAULT 'Ida',
  `puntos_json` JSON NOT NULL,
  `camino_geom` LINESTRING NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `linea_id` (`linea_id` ASC) VISIBLE,
  CONSTRAINT `trazados_geo_ibfk_1`
    FOREIGN KEY (`linea_id`)
    REFERENCES `colectigo`.`lineas` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
