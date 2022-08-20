-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema peanobar
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema peanobar
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `peanobar` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `peanobar` ;

-- -----------------------------------------------------
-- Table `peanobar`.`api`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`api` (
  `uuid` BINARY(16) NOT NULL,
  `apikey` CHAR(60) NULL DEFAULT NULL,
  `issued_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `peanobar`.`images`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`images` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `data` MEDIUMBLOB NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `peanobar`.`order_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`order_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `quantity` TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `peanobar`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_uuid` BINARY(16) NOT NULL,
  `total` INT UNSIGNED NULL DEFAULT NULL,
  `made_at` DATETIME NOT NULL,
  `status` ENUM('IN_CART', 'IN_PROGRESS', 'COMPLETED') NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `peanobar`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `cost` INT NOT NULL DEFAULT '-1',
  `img_id` BIGINT NULL DEFAULT NULL,
  `type` VARCHAR(20) CHARACTER SET 'utf8mb3' NOT NULL DEFAULT 'PANINO',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `peanobar`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

INSERT IGNORE INTO `peanobar`.`roles` VALUES (1,'ROLE_USER'),(2,'ROLE_BAR'),(3,'ROLE_ADMIN');


-- -----------------------------------------------------
-- Table `peanobar`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`users` (
  `uuid` BINARY(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID())),
  `name` VARCHAR(128) CHARACTER SET 'utf8mb3' NULL DEFAULT NULL,
  `username` VARCHAR(128) CHARACTER SET 'utf8mb3' NOT NULL,
  `password` CHAR(60) NOT NULL,
  `role_id` BIGINT NOT NULL DEFAULT '0',
  `email` VARCHAR(128) CHARACTER SET 'utf8mb3' NULL DEFAULT NULL,
  `classroom` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE INDEX `id_UNIQUE` (`uuid` ASC) VISIBLE,
  UNIQUE INDEX `password_UNIQUE` (`password` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

INSERT IGNORE INTO `peanobar`.`users` VALUES (UNHEX('0000000000000000'),'admin','admin','$2a$12$x3KHnxGvph8CQqfU.Se3T.Wm1eAEY.76pwIm/BUPK9sCnhVdAaCta',3,NULL,0);


-- -----------------------------------------------------
-- Table `peanobar`.`wallets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peanobar`.`wallets` (
  `uuid` BINARY(16) NOT NULL,
  `balance` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

INSERT IGNORE INTO `peanobar`.`wallets` VALUES (UNHEX('0000000000000000'),0);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
