# Student Attendance Management System

A simple desktop application to manage student records and daily attendance,
built as a third-year engineering mini-project using **Core Java, JDBC, MySQL,
and Java Swing**.

---

## 📌 Project Overview

This project allows a college admin/faculty member to:

- Log in securely (admin login)
- Add, update, delete, and view student records
- Mark daily attendance (Present/Absent) subject-wise
- Search students by Name or Roll Number
- View attendance history for any student
- Calculate attendance percentage
- Generate a monthly attendance summary for all students

The project intentionally avoids advanced frameworks (Spring Boot, Hibernate,
React, etc.) and sticks to **plain Java + JDBC + Swing**, since that is what
most colleges expect for a DBMS / OOPS mini-project in the third year.

---

## 🛠️ Tech Stack

| Layer       | Technology         |
|-------------|---------------------|
| Language    | Java (JDK 8 or above) |
| Database    | MySQL               |
| DB Connectivity | JDBC (mysql-connector-j) |
| GUI         | Java Swing           |
| IDE used    | Eclipse / IntelliJ / VS Code (any) |

---

## 📂 Project Structure

```
StudentAttendanceSystem/
│
├── src/
│   ├── DatabaseConnection.java   # Handles JDBC connection to MySQL
│   ├── Login.java                 # Admin login screen (entry point - main method here)
│   ├── Student.java               # Student model + Student Management GUI (CRUD)
│   ├── Attendance.java            # Attendance model + Mark Attendance + Reports GUI
│   └── MainMenu.java              # Dashboard after login, navigates to other modules
│
├── sql/
│   └── database_setup.sql         # Creates database, tables, and inserts sample data
│
├── lib/
│   └── (place mysql-connector-j-x.x.x.jar here)
│
└── README.md
```

---

## ⚙️ Setup Instructions

### 1. Install Prerequisites
- Install **JDK 8+** and make sure `java`/`javac` work from the terminal.
- Install **MySQL Server** (MySQL Workbench is helpful but optional).
- Download the **MySQL Connector/J** JDBC driver (`mysql-connector-j-x.x.x.jar`)
  from https://dev.mysql.com/downloads/connector/j/ and place it inside the
  `lib/` folder.

### 2. Set up the Database
1. Open MySQL Workbench / MySQL command line.
2. Run the script provided in `sql/database_setup.sql`:
   ```sql
   SOURCE path/to/sql/database_setup.sql;
   ```
   This will:
   - Create the database `attendance_management_system`
   - Create the `students`, `attendance`, and `admin` tables
   - Insert sample test data (6 students, sample attendance records,
     and a default admin login)

### 3. Configure Database Credentials
Open `src/DatabaseConnection.java` and update these lines with your own
MySQL username and password:
```java
private static final String USER = "root";
private static final String PASSWORD = "root";
```

### 4. Compile and Run

**Using terminal (Windows example):**
```bash
cd StudentAttendanceSystem/src
javac -cp ".;../lib/mysql-connector-j-8.x.x.jar" *.java
java -cp ".;../lib/mysql-connector-j-8.x.x.jar" Login
```

**Using terminal (Linux/Mac example):**
```bash
cd StudentAttendanceSystem/src
javac -cp ".:../lib/mysql-connector-j-8.x.x.jar" *.java
java -cp ".:../lib/mysql-connector-j-8.x.x.jar" Login
```

**Using an IDE (Eclipse/IntelliJ):**
1. Create a new Java Project and add all files from `src/` into it.
2. Add the MySQL connector JAR to the project's build path / external libraries.
3. Run `Login.java` (it contains the `main` method).

---

## 🔑 Default Login Credentials

| Username | Password |
|----------|----------|
| admin    | admin123 |

(These credentials work as a hardcoded fallback even if the `admin` table
is not yet set up, and are also stored in the database via the SQL script.)

---

## 🧪 Sample Test Data

The SQL script inserts 6 sample students across CSE, ECE, and Mechanical
departments, along with sample attendance entries for June 2026, so you
can test search, history, percentage calculation, and the monthly summary
feature right after setup without manually entering data.

---

## 🚀 How to Use

1. Run `Login.java` → log in with admin credentials.
2. From the **Main Menu**, choose:
   - **Manage Students** → Add/Update/Delete/View/Search student records.
   - **Manage Attendance** → Mark attendance for a student, view their
     history, calculate their attendance %, or generate a monthly summary
     for all students.
3. Use **Logout** to return to the login screen, or **Exit** to close the app.

---

## 📋 Features Checklist

- [x] Admin Login (hardcoded + database-based)
- [x] Add / Update / Delete / View Student records
- [x] Mark attendance (Present/Absent) by date and subject
- [x] Search student by Name
- [x] Search student by Roll Number
- [x] View attendance history per student
- [x] Calculate attendance percentage (with low-attendance warning < 75%)
- [x] Monthly attendance summary report for all students

---

## 💡 Possible Future Enhancements

- Export attendance reports to PDF/Excel
- Faculty-specific login (multiple subject teachers)
- Attendance charts/graphs using JFreeChart
- Email/SMS alerts for low attendance
- Password hashing for admin login (currently stored as plain text,
  which is fine for a college mini-project but not production-safe)

---

## 👨‍🎓 Notes for Viva / Interview

- The project uses **JDBC PreparedStatements** throughout to prevent SQL
  injection and handle parameterized queries safely.
- **OOP concepts used:** Encapsulation (private fields with getters/setters
  in `Student.java` and `Attendance.java`), separation of concerns
  (`DatabaseConnection` handles only DB connectivity), and event-driven
  programming via `ActionListener`.
- The `students` and `attendance` tables are linked using a **foreign key**
  (`student_id`) with `ON DELETE CASCADE`, so deleting a student
  automatically removes their attendance records.
- GUI uses Java Swing with `null` layout (absolute positioning) — a common,
  simple approach for mini-projects, though `GridBagLayout` or `BoxLayout`
  would be used in a more polished real-world app.

---

## 📄 License

This project is created for educational purposes as part of a college
mini-project / internship portfolio. Free to use and modify.
