# 🎓 Enterprise Online Examination System

[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/Dipensah4/Online-Exam-System-Exam-Portal/graphs/commit-activity)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5+-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.java.com/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)

An enterprise-grade, full-stack examination management platform. Built with **Spring Boot 3.5**, this system provides a secure, scalable, and highly interactive environment for educational institutions to conduct digital assessments.

---

## 💎 Premium Experience

The system has been recently modernized to an **Enterprise Level** aesthetic, featuring:

- **Executive Dashboards**: High-impact data visualization for both administrators and students.
- **Glassmorphism UI**: A state-of-the-art interface using blurred backgrounds and refined shadows.
- **Unified Design System**: A consistent, premium Slate/Blue color palette across all modules.
- **Micro-interactivity**: Smooth transitions and hover effects for a responsive user experience.

---

## 🚀 Strategic Features

### 💻 Administrative Core

- **Intelligent Exam Architect**: Create complex exams with dynamic time windows and passing criteria.
- **Hierarchical Question Bank**: Manage vast repositories of questions with difficulty-based categorization.
- **Enterprise Import**: High-speed batch processing of questions via Excel integration.
- **Executive Analytics**: Real-time monitoring of student performance, submission audit logs, and student-specific result drill-downs.
- **Show Result Integration**: One-click access to student performance history directly from the student registry.
- **Competitive Leaderboards**: Dynamic ranking system to foster excellence.

### 🎓 Student Hub

- **Personalized Learning Portal**: Focused dashboard for upcoming assessments and performance history.
- **Show Result Interface**: Prompt access to overall performance metrics and historical results directly from the dashboard.
- **Advanced Examination Hall**: Multi-functional test environment with real-time timers and result reviews.
- **Secure Credentials**: Robust role-based access control protecting sensitive student data.
- **Global Achievement Map**: Visualize progress across the curriculum through interactive scoreboards.

---

## 🛠 Technology Architecture

| Layer              | Technologies                                                                           |
| :----------------- | :------------------------------------------------------------------------------------- |
| **Backend**        | Spring Boot 3.5, Spring Security, Spring Data JPA, Hibernate                           |
| **Frontend**       | Thymeleaf, Vanilla CSS (Modern CSS3), JavaScript (ES6+), Inter & Poppins Typography    |
| **Infrastructure** | MySQL 8.0, Maven 3.x, Docker                                                           |
| **Libraries**      | Apache POI (Excel Logic), Lombok, MapStruct (DTO Mapping), Springdoc OpenAPI (Swagger) |

---

## 📂 Architecture Overview

The project follows a standard N-tier architecture, ensuring strict separation of concerns and high maintainability.

```text
online-exam-system/
├── Dockerfile           # Optimized Multi-stage Build Profile
├── pom.xml              # Maven dependencies & build config
└── src/main/java/com/exam/online_exam_system/
    ├── config/          # System configuration & data initializers
    ├── controller/      # Web Entry Points (Admin & Student controllers)
    ├── dto/             # Data Transfer Objects for decoupled requests
    ├── exception/       # Global exception handling & custom errors
    ├── model/           # JPA Entities (Exam, Student, Question, User, etc.)
    ├── repository/      # Data Access Layer (Spring Data JPA)
    ├── security/        # Security configuration & user details service
    ├── service/         # Business Logic implementation
    ├── util/            # Helper classes and static utilities
    └── OnlineExamSystemApplication.java # System Bootstrap
└── src/main/resources/
    ├── static/          # UI Assets (CSS, JS, Images)
    ├── templates/       # Thymeleaf View Layers (Fragments, Admin, Student)
    └── application.properties # Core system configurations
```

---

## 🚀 Getting Started

### 🏠 Local Development

1. **Prepare Database**:
   ```sql
   CREATE DATABASE examdb;
   ```
2. **Environment Configuration**:
   Update `application.properties` with your local credentials.
3. **Boot Application**:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🔑 Access Matrix

| Account Type   | Standard Identity | Initial Token |
| :------------- | :---------------- | :------------ |
| **Root Admin** | `admin`           | `admin123`    |

> [!TIP]
> Students can register directly through the **Sign Up** page to create their personal accounts.

---

## 🤝 Authors

- **Dipen Sah** - _Core Architecture & UI Modernization_
- **Ayush Mishra** - _Co-Author_
- **Prakash Kumar Mahato** - _Co-Author_
- **Ujwal Mandal** - _Co-Author_

---

## 📄 License & Terms

Licensed under **Educational Commons**. Unauthorized commercial distribution is prohibited.
