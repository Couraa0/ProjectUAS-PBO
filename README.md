# Project Sistem Parkir Berbasis GUI Java

**Sistem Parkir Berbasis GUI Java** adalah aplikasi desktop yang dikembangkan sebagai tugas akhir mata kuliah *Pemrograman Berorientasi Objek* (PBO) di Universitas Singaperbangsa Karawang. Project ini dirancang untuk mengelola sistem manajemen parkir modern dengan fitur lengkap, mulai dari pencatatan kendaraan masuk dan keluar, perhitungan tarif otomatis, hingga pencetakan struk parkir dalam format PDF. Aplikasi ini menggunakan Java Swing untuk antarmuka grafis dan SQLite sebagai basis data lokal, serta menerapkan konsep OOP dan beberapa design pattern.

## 👥 Tim Pengembang

**Kelompok 5 PBO**  
Project UAS Mata Kuliah Pemrograman Berorientasi Objek   
[Laporan Project UAS PBO (Google Drive)](https://drive.google.com/file/d/1jycHUn044L1JcQkgZvI3uwzJ9HJT2EMq/view?usp=sharing)

### Anggota Kelompok

| Nama Lengkap         | NPM           | GitHub Profile                |
|----------------------|---------------|-------------------------------|
| M Rakha Syamputra    | 2310631250024 | [@couraa0](https://github.com/couraa0)      |
| Aditya Tazkia Aulia  | 2310631250003 | [@ditytzk](https://github.com/ditytzk)      |
| Rizky Azhari Putra   | 2310631250028 | [@rizky161004](https://github.com/rizky161004)     |
| Freidrick Albert P   | 2301631250058 | [@freidrickalbert](https://github.com/freidrickalbert)   |

## 📋 Fitur Utama

- **Login System**: Autentikasi dengan role-based access (Admin/Operator)
- **Manajemen Kendaraan Masuk**: Form kendaraan masuk (GUI modern)
- **Manajemen Kendaraan Keluar**: Form kendaraan keluar (GUI modern)
- **Sistem Tarif Otomatis**: Tarif berbeda untuk Motor & Mobil, hitung durasi & inap
- **Database Integration**: SQLite (JDBC)
- **Search & Filter**: Cari dan urutkan data parkir
- **Print Struk**: Cetak struk parkir dalam format PDF
- **Admin Features**: Edit & hapus data (khusus admin)
- **UI Modern**: Desain antarmuka profesional

## 🏗️ Struktur Project

```
src/
└── Uas_Pbo/
    ├── DBConnection.java
    ├── EditDialog.java
    ├── Kendaraan.java
    ├── Keluar.java
    ├── LoginForm.java
    ├── Main.java
    ├── Masuk.java
    ├── Mobil.java
    ├── Motor.java
    ├── OptionOP.java
    ├── ParkirAppSystem.java
    ├── PrintDialog.java
    ├── users.db
    ├── parkir.db
    ├── README.md
    └── img/
        ├── parkir.png
        └── parkir2.png
```

## 🛠️ Teknologi yang Digunakan

- **Java 8+**: Bahasa pemrograman utama
- **Swing**: GUI framework
- **SQLite**: Database lokal
- **JDBC**: Database connectivity
- **iText**: Library untuk export PDF struk parkir

## 📋 Persyaratan Sistem

- Java Development Kit (JDK) 8 atau lebih tinggi
- SQLite JDBC Driver (`sqlite-jdbc-x.x.x.jar`)
- iText PDF Library (`itextpdf-x.x.x.jar`)
- IDE Java (NetBeans, Eclipse, IntelliJ IDEA, dsb.)

## 🚀 Cara Menjalankan

### 1. Setup Database & Library
- Download SQLite JDBC Driver dan iText PDF Library
- Tambahkan `sqlite-jdbc-x.x.x.jar` dan `itextpdf-x.x.x.jar` ke classpath project

### 2. Compile dan Run
```bash
cd src
javac -cp ".;../sqlite-jdbc-x.x.x.jar;../itextpdf-x.x.x.jar" Uas_Pbo/*.java
java -cp ".;../sqlite-jdbc-x.x.x.jar;../itextpdf-x.x.x.jar" Uas_Pbo.Main
```

### 3. Login
**Default Accounts:**
- **Admin**: username: `admin`, password: `admin`
- **Operator**: username: `Rakha`, password: `Rakha123`

## 📱 Panduan Penggunaan

### Login
1. Jalankan aplikasi
2. Masukkan username & password
3. Klik "LOGIN"

### Kendaraan Masuk (Operator/Admin)
1. Pilih menu "Kendaraan Masuk" (atau menu operator)
2. Masukkan plat nomor & jenis kendaraan
3. Klik "Simpan Data"

### Kendaraan Keluar (Operator/Admin)
1. Pilih menu "Kendaraan Keluar" (atau menu operator)
2. Masukkan plat nomor atau klik data di tabel
3. Klik "Proses Keluar"
4. Struk otomatis tampil

### Fitur Admin
- **Edit Data**: Pilih baris → Menu → Edit Data Parkir
- **Hapus Data**: Pilih baris → Menu → Hapus Data
- **Cetak Struk**: Pilih baris → Menu → Cetak Struk

## 💰 Sistem Tarif

### Motor
- **Per jam**: Rp 2.000
- **Maksimal per hari**: Rp 15.000
- **Tarif inap**: Rp 20.000 per hari
- **Minimum**: Rp 2.000

### Mobil
- **Per jam**: Rp 5.000
- **Maksimal per hari**: Rp 30.000
- **Tarif inap**: Rp 40.000 per hari
- **Minimum**: Rp 5.000

## 🗄️ Database Schema

### Tabel `users`
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'operator'
);
```

### Tabel `parkir`
```sql
CREATE TABLE parkir (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    plat TEXT NOT NULL,
    jenis TEXT NOT NULL,
    tanggal_masuk TEXT NOT NULL,
    tanggal_keluar TEXT,
    durasi_menit INTEGER,
    tarif INTEGER
);
```

### Class Hierarchy
```
Kendaraan (Abstract)
├── Motor
└── Mobil

JFrame
├── LoginForm
├── ParkirAppSystem
├── Masuk
├── Keluar
├── OptionOP
└── JDialog
    ├── EditDialog
    └── PrintDialog
```

## 🔧 Konfigurasi

### Database Connection
File: `src/Uas_Pbo/DBConnection.java`
```java
public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db_parkir";
        String user = "root";
        String pass = "";
        return DriverManager.getConnection(url, user, pass);
    }
}
```

## 🐛 Troubleshooting

### Error "SQLite JDBC driver tidak ditemukan"
**Solusi**: Pastikan `sqlite-jdbc.jar` ada di classpath

### Error "iText PDF library tidak ditemukan"
**Solusi**: Pastikan `itextpdf-x.x.x.jar` ada di classpath

### Database tidak bisa dibuat
**Solusi**: Periksa permission folder aplikasi

### Aplikasi tidak bisa login
**Solusi**: Periksa koneksi database & data user default

## 📄 Lisensi

Project ini dikembangkan untuk keperluan pembelajaran dan tugas akhir mata kuliah Pemrograman Berorientasi Objek.  
Segala bentuk penggunaan di luar pembelajaran harap mencantumkan atribusi kepada pengembang.

---

**Happy Coding & Good Luck! 🚀**
