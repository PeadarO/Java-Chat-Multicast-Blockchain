-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-02-2020 a las 18:38:48
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
-- Estructura de tabla para la tabla `petitions`
--

DROP TABLE IF EXISTS `petitions`;
CREATE TABLE IF NOT EXISTS `petitions` (
  `password` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `room_key` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `PORT` int(10) NOT NULL,
  PRIMARY KEY (`password`),
  UNIQUE KEY `PORT` (`PORT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `petitions`
--

INSERT INTO `petitions` (`password`, `room_key`, `PORT`) VALUES
('12345', 'P0ESJDIM0S8XFVCO', 5555),
('5432', 'HA6SB1IWOHP2NABT', 5544),
('98765', 'QA4JGBV2114I6YV6', 8888);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resetpassword`
--

DROP TABLE IF EXISTS `resetpassword`;
CREATE TABLE IF NOT EXISTS `resetpassword` (
  `number` int(9) NOT NULL,
  `codigo` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  UNIQUE KEY `number_2` (`number`),
  KEY `number` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `resetpassword`
--

INSERT INTO `resetpassword` (`number`, `codigo`) VALUES
(98, 'a8c2709c9d4bbb87e5d19dedffe0364174700ff7ff9aa8de56dcdc99018d1eb6');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `number` int(9) NOT NULL,
  `email` varchar(55) COLLATE utf8_spanish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `name` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`number`, `email`, `password`, `name`, `status`) VALUES
(90, 'javier@javier.gmail.com', '69f59c273b6e669ac32a6dd5e1b2cb63333d8b004f9696447aee2d422ce63763', 'Javier', 1),
(98, 'canarygo1@gmail.com', '3514acf61732f662da19625f7fe781c3e483f2dce8506012f3bb393f5003e105', 'wer', 0),
(123, 'w', 'prueba', 'w', 0),
(543, 'michela@michela.com', '18beb4813723e788a1d79bcbf80802538ec813aa19ded2e9c21cbf08bed6bee3', 'michela', 1),
(876, 'alfredo@gmail.com', 'a73ab888363736220eb589458721088241ee10059b1f5898a13fe9c2e14fcd8c', 'Alfredo', 0),
(987, 'mic', '224948ca810fc2bc9e3d80c0b1129fe2487fa4e307961001ab8443db82f7bb69', 'mic', 0),
(4321, 'we@we.com', 'dc7c811b9561739d9b75bb3e9e1715970a868834e62251b0b9ca02e74d0f42c9', 'we', 0),
(54321, 'w@s.com', '20f3765880a5c269b747e1e906054a4b4a3a991259f1e16b5dde4742cec2319a', 'w', 0),
(123456789, 'powerkun19@gmail.com', 'dc7c811b9561739d9b75bb3e9e1715970a868834e62251b0b9ca02e74d0f42c9', 'javi', 0);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `resetpassword`
--
ALTER TABLE `resetpassword`
  ADD CONSTRAINT `resetpassword_ibfk_1` FOREIGN KEY (`number`) REFERENCES `users` (`number`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
