-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-02-2020 a las 19:48:20
-- Versión del servidor: 10.1.38-MariaDB
-- Versión de PHP: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `javachat`
--
CREATE DATABASE IF NOT EXISTS `javachat` DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish_ci;
USE `javachat`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `peticiones`
--

DROP TABLE IF EXISTS `peticiones`;
CREATE TABLE IF NOT EXISTS `peticiones` (
  `ref_1` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `ref_2` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `resetPassword`;


CREATE TABLE IF NOT EXISTS `resetPassword`(
 `number` int(9) NOT NULL unique,
 `codigo` varchar(255) NOT NULL
);
select * from resetPassword;

CREATE TABLE IF NOT EXISTS `users` (
  `number` int(9) NOT NULL,
  `username` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `name` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `users`
--
ALTER TABLE resetPassword
ADD FOREIGN KEY (number) REFERENCES users(number);

INSERT INTO `resetPassword` (`number`, `codigo`) VALUES
(123, 'hola');


Update users inner join resetPassword  on users.number = resetPassword.number set users.password = 'prueba' where resetPassword.codigo ='hola';

INSERT INTO `users` (`number`, `username`, `password`, `name`, `status`) VALUES
(123, 'w', '50e721e49c013f00c62cf59f2163542a9d8df02464efeb615d31051b0fddc326', 'w', 0),
(987, 'mic', '224948ca810fc2bc9e3d80c0b1129fe2487fa4e307961001ab8443db82f7bb69', 'mic', 0),
(4321, 'we', 'dc7c811b9561739d9b75bb3e9e1715970a868834e62251b0b9ca02e74d0f42c9', 'we', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
