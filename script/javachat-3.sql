-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 03-02-2020 a las 22:16:45
-- Versión del servidor: 10.1.38-MariaDB
-- Versión de PHP: 7.1.26

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
-- Estructura de tabla para la tabla `chat`
--

DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `idUsuarioEnv` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `idUsuarioRec` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `Mensaje` text COLLATE utf8_spanish_ci NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- RELACIONES PARA LA TABLA `chat`:
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `peticiones`
--

DROP TABLE IF EXISTS `peticiones`;
CREATE TABLE `peticiones` (
  `ref_1` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `ref_2` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- RELACIONES PARA LA TABLA `peticiones`:
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resetPassword`
--

DROP TABLE IF EXISTS `resetPassword`;
CREATE TABLE `resetPassword` (
  `number` int(9) NOT NULL,
  `codigo` varchar(25) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- RELACIONES PARA LA TABLA `resetPassword`:
--   `number`
--       `users` -> `number`
--

--
-- Volcado de datos para la tabla `resetPassword`
--

INSERT INTO `resetPassword` (`number`, `codigo`) VALUES
(123, 'hola');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `number` int(9) NOT NULL,
  `username` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `name` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- RELACIONES PARA LA TABLA `users`:
--

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`number`, `username`, `password`, `name`, `status`) VALUES
(123, 'w', 'prueba', 'w', 0),
(987, 'mic', '224948ca810fc2bc9e3d80c0b1129fe2487fa4e307961001ab8443db82f7bb69', 'mic', 0),
(4321, 'we', 'dc7c811b9561739d9b75bb3e9e1715970a868834e62251b0b9ca02e74d0f42c9', 'we', 0);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `resetPassword`
--
ALTER TABLE `resetPassword`
  ADD KEY `number` (`number`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`number`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `resetPassword`
--
ALTER TABLE `resetPassword`
  ADD CONSTRAINT `resetpassword_ibfk_1` FOREIGN KEY (`number`) REFERENCES `users` (`number`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
