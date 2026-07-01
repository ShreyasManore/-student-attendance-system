-- ============================================================
-- Student Attendance Management System
-- Database Creation Script
-- ============================================================
-- Author: (Your Name)
-- Description: Creates the database, tables, and inserts sample
--              test data for the Student Attendance Management
--              System mini-project.
-- ============================================================

-- Create database (only if it does not already exist)
CREATE DATABASE IF NOT EXISTS attendance_management_system;

-- Select the database to use
USE attendance_management_system;

-- ------------------------------------------------------------
-- Table: students
-- Stores basic information about each student
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id   INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    roll_no      VARCHAR(20)  NOT NULL UNIQUE,
    department   VARCHAR(50)  NOT NULL,
    year         INT          NOT NULL,
    email        VARCHAR(100)
);

-- ------------------------------------------------------------
-- Table: attendance
-- Stores attendance records for each student
-- Linked to students table using student_id (foreign key)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id    INT NOT NULL,
    date          DATE NOT NULL,
    status        VARCHAR(10) NOT NULL,   -- 'Present' or 'Absent'
    subject       VARCHAR(50) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Table: admin
-- Stores admin login credentials (used for database-based login)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- ============================================================
-- SAMPLE / TEST DATA
-- ============================================================

-- Default admin login (username: admin / password: admin123)
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- Sample students
INSERT INTO students (name, roll_no, department, year, email) VALUES
('Rahul Sharma',   'CSE001', 'Computer Science', 3, 'rahul.sharma@example.com'),
('Priya Patel',    'CSE002', 'Computer Science', 3, 'priya.patel@example.com'),
('Amit Kumar',     'CSE003', 'Computer Science', 3, 'amit.kumar@example.com'),
('Sneha Reddy',    'ECE001', 'Electronics',      3, 'sneha.reddy@example.com'),
('Vikram Singh',   'ECE002', 'Electronics',      3, 'vikram.singh@example.com'),
('Ananya Iyer',    'MECH001','Mechanical',       3, 'ananya.iyer@example.com');

-- Sample attendance records (using student_id 1 to 6)
INSERT INTO attendance (student_id, date, status, subject) VALUES
(1, '2026-06-01', 'Present', 'Database Management Systems'),
(1, '2026-06-02', 'Present', 'Database Management Systems'),
(1, '2026-06-03', 'Absent',  'Database Management Systems'),
(2, '2026-06-01', 'Present', 'Database Management Systems'),
(2, '2026-06-02', 'Absent',  'Database Management Systems'),
(2, '2026-06-03', 'Present', 'Database Management Systems'),
(3, '2026-06-01', 'Absent',  'Operating Systems'),
(3, '2026-06-02', 'Present', 'Operating Systems'),
(4, '2026-06-01', 'Present', 'Digital Electronics'),
(4, '2026-06-02', 'Present', 'Digital Electronics'),
(5, '2026-06-01', 'Present', 'Digital Electronics'),
(6, '2026-06-01', 'Absent',  'Thermodynamics');

-- ============================================================
-- End of script
-- ============================================================
