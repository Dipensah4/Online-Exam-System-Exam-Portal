# 🎓 Online Student Examination System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.java.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red)](https://maven.apache.org/)

A comprehensive, web-based platform designed to streamline the examination process. This system allows administrators to create and manage exams effectively, while offering students a seamless interface to register, participate in scheduled tests, and track their performance through real-time results and leaderboards.

---

## 🚀 Key Features

### 🛠 Admin Features
- **Exam Management**: Effortlessly create, edit, schedule, and delete exams.
- **Excel Question Import**: Batch upload questions using Excel spreadsheets to save time.
- **Customizable Grading**: Set dynamic passing percentages and assign difficulty levels (**Easy**, **Medium**, **Hard**) with specific marks for each question.
- **Student Monitoring**: Manage registered students and audit detailed submission histories.
- **Real-time Leaderboard**: Monitor competitive student rankings across all examinations.
- **Profile Management**: Update administrative details and security settings from a dedicated profile section.

### 🎓 Student Features
- **Secure Authentication**: Robust registration and login system.
- **Interactive Dashboard**: A personalized portal showing upcoming, active, and completed exams.
- **Real-time Evaluation**: Instant score calculation and a detailed review of submitted answers.
- **Success Tracking**: View comprehensive performance history and global leaderboards.
- **Dynamic Profile**: Manage personal information and track academic progress.

---

## 🛠 Technology Stack

- **Backend**: [Spring Boot](https://spring.io/projects/spring-boot) (Java)
- **Frontend**: [Thymeleaf](https://www.thymeleaf.org/) (Template Engine), HTML5, Vanilla CSS, JavaScript (ES6+)
- **Database**: [MySQL](https://www.mysql.com/)
- **Build Tool**: [Maven](https://maven.apache.org/)
- **Libraries**:
  - **Apache POI**: For advanced Excel processing and question imports.
  - **Spring Security**: For enterprise-grade authentication and role-based access control.
  - **Lombok**: To reduce boilerplate code and improve maintainability.

---

## 📂 Project Structure

```text
online-exam-system/
├── src/
│   ├── main/
│   │   ├── java/com/exam/online_exam_system/
│   │   │   ├── controller/      # Web Controllers (MVC & REST)
│   │   │   ├── service/         # Business Logic Layer
│   │   │   ├── repository/      # Data Access Objects (Spring Data JPA)
│   │   │   ├── model/           # Entity Models (JPA/Hibernate)
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Custom Exception Handlers
│   │   │   └── util/            # Utility Helper Classes
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf HTML Templates
│   │       ├── static/          # CSS, JS, and UI Assets
│   │       └── application.properties # Core System Configurations
│   └── test/                    # Unit and Integration Tests
├── pom.xml                      # Maven Dependency Management
├── mvnw                         # Maven Wrapper Script
└── README.md                    # Project Documentation
```

---

## ⚙️ Database Setup

1. **Create the Database**:
   ```sql
   CREATE DATABASE online_exam_system;
   ```

2. **Configure Connection**:
   Update your `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   # -- Database --
   spring.datasource.url=jdbc:mysql://localhost:3306/examdb
   spring.datasource.username=${DB_USERNAME:root}
   spring.datasource.password=${DB_PASSWORD:Dipen@3575}

   # -- JPA / Hibernate --
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.open-in-view=false

   # -- Server --
   server.port=8080
   ```

---

## 🏃 How to Run the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Dipensah4/Online-Exam-System-Exam-Portal.git
   ```
2. **Open the project** in your preferred IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code).
3. **Configure MySQL** as outlined in the [Database Setup](#-database-setup) section.
4. **Build and Run**: Locate the `OnlineExamSystemApplication.java` file and run it as a Spring Boot Application.
5. **Access the App**: Navigate to [http://localhost:8080](http://localhost:8080) in your web browser.

---

## 🔑 Default Login Credentials

| Role | Username | Password |
| :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` |
| **Student** | `student1` | `student123` |

> [!NOTE]
> New students can register directly through the **Registration Page** and create their own accounts.

---

## 📊 Excel Question Import Format

To import questions accurately via Excel, ensure your spreadsheet follows this column configuration:
`Question`, `OptionA`, `OptionB`, `OptionC`, `OptionD`, `CorrectAnswer`, `Difficulty`, `Marks`, `Subject`, `ExamName`.

---

## 📅 Exam Scheduling & Monitoring

The system features a sophisticated scheduling engine that tracks:
- **Active Window**: Students can only access exams during the scheduled **Date**, **Start Time**, and **End Time**.
- **Missed Exam Detection**: If an exam is not attempted within the scheduled window, the system automatically flags it as **MISSED**, ensuring integrity and accuracy in performance reporting.

---

## 📸 Screenshots

- **Login Page**: Sleek, modern authentication interface with a premium feel.
- **Admin Dashboard**: A high-level overview of system metrics and administrative tools.
- **Student Dashboard**: A focused portal for upcoming tasks and personal progress.
- **Exam Page**: An interactive, distraction-free environment for test-taking.
- **Result Page**: Comprehensive data visualization of test scores and performance.
- **Leaderboard**: A dynamic, competitive ranking system updated in real-time.

---

## 🔮 Future Improvements

- [ ] **Email Notifications**: Automated alerts for schedule changes and result publications.
- [ ] **AI Proctoring**: Integration of camera monitoring and tab-switch prevention.
- [ ] **Advanced Randomization**: Unique question sequences for every student attempt.

---

## ✍️ Authors

Developed with ❤️ by:
- **Dipen Sah** (Lead Developer)
- **Ayush Mishra** (Co-Author)
- **Prakash Kumar Mahato** (Co-Author)
- **Ujwal Mandal** (Co-Author)

---

## 📄 License
This project is licensed for **educational purposes only**.
