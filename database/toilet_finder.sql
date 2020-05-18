-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 19, 2020 at 12:11 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `toilet_finder`
--

-- --------------------------------------------------------

--
-- Table structure for table `ratings`
--

CREATE TABLE `ratings` (
  `id_user` int(10) NOT NULL,
  `id_toilet` int(10) NOT NULL,
  `id_rating` int(10) NOT NULL,
  `ratingNumber` float(5,2) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ratings`
--

INSERT INTO `ratings` (`id_user`, `id_toilet`, `id_rating`, `ratingNumber`, `date`) VALUES
(93, 11, 111, 4.50, '2020-05-18'),
(93, 12, 100, 5.00, '2020-05-17'),
(93, 25, 106, 4.50, '2020-05-18'),
(93, 28, 107, 5.00, '2020-05-18'),
(94, 12, 103, 5.00, '2020-05-17'),
(96, 12, 101, 5.00, '2020-05-17'),
(97, 28, 109, 4.50, '2020-05-18');

-- --------------------------------------------------------

--
-- Table structure for table `rating_dan_review`
--

CREATE TABLE `rating_dan_review` (
  `id_user` int(10) NOT NULL,
  `id_toilet` int(10) NOT NULL,
  `rating` float(5,2) NOT NULL,
  `review` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id_rating` int(10) NOT NULL,
  `id_review` int(10) NOT NULL,
  `ratingText` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id_rating`, `id_review`, `ratingText`) VALUES
(101, 25, 'wow'),
(109, 26, 'Keren Bro Toiletnya');

-- --------------------------------------------------------

--
-- Table structure for table `toilet`
--

CREATE TABLE `toilet` (
  `id_toilet` int(10) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `jam_buka` time NOT NULL,
  `jam_tutup` time NOT NULL,
  `gender` text NOT NULL,
  `tipe` varchar(10) NOT NULL,
  `latitude` float(10,6) NOT NULL,
  `longitude` float(10,6) NOT NULL,
  `isApproved` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `toilet`
--

INSERT INTO `toilet` (`id_toilet`, `nama`, `jam_buka`, `jam_tutup`, `gender`, `tipe`, `latitude`, `longitude`, `isApproved`) VALUES
(9, 'Toilet Biologi 1', '12:30:00', '22:00:00', 'Male', 'Public', -6.557737, 106.724182, 1),
(10, 'Toilet FMIPA', '08:00:00', '21:00:00', 'Male/Female', 'Public', -6.558429, 106.730949, 1),
(11, 'Toilet Statistika', '08:00:00', '15:00:00', 'Female', 'Staff', -6.557906, 106.730675, 1),
(12, 'Toilet Matematika', '08:00:00', '12:00:00', 'Male', 'Public', -6.557386, 106.730774, 1),
(13, 'Toilet GOR Lama', '08:00:00', '22:00:00', 'Male/Female', 'Public', -6.556454, 106.724648, 1),
(14, 'Toilet Al-Hurriyyah', '00:00:00', '23:59:59', 'Male/Female', 'Public', -6.555628, 106.725548, 1),
(15, 'Toilet Auditorium Toyib', '08:00:00', '20:00:00', 'Male/Female', 'Public', -6.558133, 106.729706, 1),
(22, 'Toilet Kosan', '08:00:00', '12:00:00', 'Female', 'Public', -6.554483, 106.736053, 1),
(23, 'Toilet Kantin', '08:00:00', '15:00:00', 'Male/Female', 'Staff', -6.555353, 106.734138, 1),
(24, 'Toilet Test', '08:00:00', '12:00:00', 'Female', 'Staff', -6.555098, 106.734009, 1),
(25, 'Toilet Test2', '08:00:00', '12:00:00', 'Male/Female', 'Staff', -6.555347, 106.733429, 1),
(26, 'Toilet Test 3', '08:00:00', '13:00:00', 'Male', 'Public', -6.555183, 106.733093, 1),
(27, 'Toilet Test4', '07:00:00', '12:00:00', 'Female', 'Public', -6.555172, 106.732803, 1),
(28, 'Toilet Test5', '12:00:00', '16:00:00', 'Male', 'Staff', -6.555112, 106.732498, 1),
(29, 'Toilet Teaching Lab', '08:00:00', '17:00:00', 'Male/Female', 'Public', -6.555322, 106.731239, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(10) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(320) NOT NULL,
  `gender` text NOT NULL,
  `foto` blob DEFAULT NULL,
  `isAdmin` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `username`, `password`, `email`, `gender`, `foto`, `isAdmin`) VALUES
(93, 'Pascal', '123', 'pascalpanatagama@gmail.com', 'Male', NULL, 1),
(94, 'UserTest', '123', '123', 'Male', NULL, 0),
(95, 'Naruto', '123', '123', 'Male', NULL, 0),
(96, 'UserTest2', '123', '123', 'Male', NULL, 0),
(97, 'Pakkun', '123', '123', 'Male', NULL, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ratings`
--
ALTER TABLE `ratings`
  ADD PRIMARY KEY (`id_user`,`id_toilet`) USING BTREE,
  ADD UNIQUE KEY `id_rating` (`id_rating`),
  ADD KEY `id_toilet` (`id_toilet`);

--
-- Indexes for table `rating_dan_review`
--
ALTER TABLE `rating_dan_review`
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_toilet` (`id_toilet`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id_review`),
  ADD KEY `id_rating` (`id_rating`);

--
-- Indexes for table `toilet`
--
ALTER TABLE `toilet`
  ADD PRIMARY KEY (`id_toilet`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `id_rating` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id_review` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `toilet`
--
ALTER TABLE `toilet`
  MODIFY `id_toilet` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=98;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
