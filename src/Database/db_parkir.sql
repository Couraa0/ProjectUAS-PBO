-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 08 Jun 2025 pada 12.43
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_parkir`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `parkir`
--

CREATE TABLE `parkir` (
  `id` int(11) NOT NULL,
  `plat` varchar(20) NOT NULL,
  `jenis` varchar(20) NOT NULL,
  `tanggal_masuk` datetime NOT NULL,
  `tanggal_keluar` datetime DEFAULT NULL,
  `durasi_menit` int(11) DEFAULT NULL,
  `tarif` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `parkir`
--

INSERT INTO `parkir` (`id`, `plat`, `jenis`, `tanggal_masuk`, `tanggal_keluar`, `durasi_menit`, `tarif`) VALUES
(1, 'B 1234 POI', 'Motor', '2025-05-22 23:39:09', '2025-05-22 23:44:56', 5, 165000),
(2, 'F 6754 KLU', 'Motor', '2025-05-22 23:39:21', '2025-05-22 23:58:55', 19, 627),
(3, 'B 1234 GHU', 'Motor', '2025-05-22 23:45:35', '2025-05-23 00:05:06', 19, 627),
(4, 'G 4567 LKJ', 'Mobil', '2025-05-22 23:45:51', '2025-05-23 22:56:19', 1390, 115370),
(5, 'Z 3987 SDF', 'Mobil', '2025-05-22 23:46:04', '2025-05-23 23:02:38', 1396, 80000),
(6, 'H 8765 MNB', 'Motor', '2025-05-22 23:50:13', '2025-05-24 10:20:34', 2070, 60000),
(7, 'B 6589 LKU', 'Mobil', '2025-05-22 23:57:45', '2025-05-24 14:29:24', 2311, 120000),
(8, 'A 4729 NGY', 'Motor', '2025-05-23 00:15:49', '2025-05-24 14:32:24', 2296, 40000),
(9, 'F 3421 FFF', 'Mobil', '2025-05-23 00:16:02', '2025-05-24 16:57:04', 2441, 80000),
(10, 'Q 3756 HHH', 'Mobil', '2025-05-23 00:16:13', '2025-05-24 17:55:14', 2499, 80000),
(11, 'A 2355 HYU', 'Motor', '2025-05-23 00:16:27', '2025-05-25 21:26:29', 4150, 60000),
(12, 'S 5642 JHY', 'Motor', '2025-05-23 00:35:37', '2025-05-24 15:04:08', 2308, 40000),
(13, 'A 1234 HJU', 'Motor', '2025-05-23 23:04:15', '2025-05-26 18:25:29', 4041, 80000),
(14, 'S 5642 POD', 'Mobil', '2025-05-23 23:04:25', '2025-05-26 19:02:00', 4077, 160000),
(15, 'Q 7612 LKJ', 'Mobil', '2025-05-23 23:04:34', '2025-05-24 15:01:30', 956, 80000),
(16, 'L 5642 SDF', 'Motor', '2025-05-23 23:04:44', '2025-05-24 10:22:17', 677, 40000),
(17, 'B 6589 LKU', 'Mobil', '2025-05-24 14:29:08', NULL, NULL, NULL),
(18, 'B 1234 BGD', 'Motor', '2025-05-24 17:02:10', NULL, NULL, NULL),
(19, '', 'Motor', '2025-05-24 17:24:51', '2025-05-24 17:25:20', 0, 2000),
(20, 'A 1456 POI', 'Motor', '2025-05-24 18:08:01', NULL, NULL, NULL),
(21, 'A 3452 FGT', 'Mobil', '2025-05-24 18:08:18', NULL, NULL, NULL),
(22, 'A 5432 POI', 'Motor', '2025-05-25 21:26:21', NULL, NULL, NULL),
(23, 'S 1234 POI', 'Motor', '2025-05-26 19:01:51', '2025-05-26 19:02:15', 0, 2000),
(24, 'D 1234 POI', 'Motor', '2025-05-26 19:02:32', NULL, NULL, NULL),
(25, 'Q 1234 POI', 'Mobil', '2025-05-26 19:02:44', '2025-05-26 19:04:22', 1, 5000),
(26, 'Q 1234 POI', 'Mobil', '2025-05-26 19:04:58', NULL, NULL, NULL),
(27, 'B 1234 LKJ', 'Motor', '2025-05-29 19:44:46', NULL, NULL, NULL),
(28, 'B 1234 POI', 'Motor', '2025-06-01 13:25:27', '2025-06-01 13:26:01', 0, 2000),
(29, 'B 2321 POL', 'Mobil', '2025-06-07 11:38:28', '2025-06-07 11:38:57', 0, 5000),
(30, '1234', 'Motor', '2025-06-07 12:17:18', '2025-06-07 12:17:29', 0, 2000),
(31, '12345', 'Motor', '2025-06-07 12:26:24', '2025-06-07 12:26:37', 0, 2000),
(32, '0987', 'Motor', '2025-06-07 12:40:38', '2025-06-07 13:34:04', 53, 2000),
(33, 'BBBB', 'Mobil', '2025-06-07 12:40:56', '2025-06-07 14:23:16', 102, 8466),
(34, 'PPP', 'Motor', '2025-06-07 13:05:49', NULL, NULL, NULL),
(35, 'B 7654 JHY', 'Motor', '2025-06-07 14:24:04', NULL, NULL, NULL),
(36, 'T 4356 UYT', 'Mobil', '2025-06-07 14:24:17', NULL, NULL, NULL),
(38, 'AAA', 'Mobil', '2025-06-07 23:03:25', '2025-06-07 23:03:42', 0, 5000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('admin','operator') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(1, 'admin', 'admin', 'admin'),
(2, 'Rakha', 'Rakha123', 'operator');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `parkir`
--
ALTER TABLE `parkir`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `parkir`
--
ALTER TABLE `parkir`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
