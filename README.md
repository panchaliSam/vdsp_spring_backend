# 📸 LensLex – Photographic Event Management System

LensLex is a full-stack event management solution tailored for photography studios to efficiently handle bookings, media, event assignments, and staff coordination. Built as part of the VDSP initiative, it empowers customers and staff through an intuitive, role-based platform.

---

## 🚀 Technologies Used

### 🖥️ Frontend (📂 VDSP_FRONTEND)
- **React.js** with **TypeScript**
- **Vite** (lightning-fast dev environment)
- **Material UI (MUI)** for responsive components
- **Axios** for API communication
- **React Router** for routing
- **Day.js** for date management
- **Hot Toast** for notifications

#### 📁 Key Folders
- `api/`: Axios-based API call logic
- `assets/`: Static files (e.g., images, logos)
- `components/`: Reusable UI components (e.g., Sidebar, Navbar)
- `context/`: Global state management (React context API)
- `helper/`: Utility logic (e.g., session checkers, validators)
- `interfaces/`: TypeScript interfaces (e.g., `UserDto`, `ReservationDto`)
- `pages/`: Core page components for each role
- `routes/`: Protected and role-based routing logic
- `utils/`: Utility functions (e.g., formatters)

---

### 🔙 Backend (📂 vdsp_spring_backend)
- **Java Spring Boot 3.x**
- **Spring Security** with JWT Auth
- **Spring Data JPA**
- **Hibernate Validator**
- **MySQL**
- **Gradle** for build automation

#### 📁 Key Packages
- `advice/`: Centralized exception handling using `@ControllerAdvice`
- `config/`: Security configuration, CORS, JWT filters
- `controller/`: REST API endpoints grouped by feature
- `dto/`: Data Transfer Objects for clean request/response
- `entity/`: JPA entities (`User`, `Album`, `Reservation`, etc.)
- `exception/`: Custom exceptions (e.g., `ResourceNotFoundException`)
- `filter/`: JWT request filter
- `helpers/`: Session and time validation logic
- `repository/`: Spring Data Repositories
- `service/`: Business logic layer
- `type/`: Enums and constants
- `utils/`: Utility classes
- `resources/`: Configuration files like `application.properties`

---

## 👤 Role-Based Access

### 🔐 Admin
- Dashboard and report generation
- Add/Manage holidays
- Manage users and roles
- Assign staff to events
- Approve/reject reservations
- View & update album statuses

### 👨‍💼 Staff
- View own assigned events (calendar)
- Change album status after editing
- Manage assigned packages
- Notification system

### 👩 Customer
- Register, login, and reserve sessions
- Upload payment proof (Full payment)
- View album and reservation status
- View payment history and receive receipts
- Receive notifications

---

## 🗂 Folder Structure Summary

### 🔹 Backend (Spring Boot)

vdsp_spring_backend/
├── src/
│   └── main/java/com/app/vdsp/
│       ├── advice/
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── exception/
│       ├── filter/
│       ├── helpers/
│       ├── repository/
│       ├── service/
│       ├── type/
│       └── utils/
└── VdspApplication.java

### 🔹 Frontend (React + Vite)

VDSP_FRONTEND/
├── src/
│   ├── api/
│   ├── assets/
│   ├── components/
│   ├── context/
│   ├── helper/
│   ├── interfaces/
│   ├── pages/
│   ├── routes/
│   └── utils/

---

## 🛠 Setup Instructions

### Backend
1. Clone the project:  
   `git clone https://github.com/your-repo/vdsp_spring_backend.git`
2. Update `.env` or `application.properties` with DB credentials
3. Run the project:  
   `./gradlew bootRun`

### Frontend
1. Clone the frontend:  
   `git clone https://github.com/your-repo/VDSP_FRONTEND.git`
2. Install dependencies:  
   `npm install`
3. Create `.env` and set `VITE_API_BASE_URL`
4. Start dev server:  
   `npm run dev`

---

## 📸 Extra Features
- Calendar view with session-based date colors and tooltips
- Album management with UUIDs
- Token-based protected routes and role management
- Real-time toast notifications
- Dynamic dropdowns and package assignments
- Optimized API response structure (`ApiResponse<T>`)

---

## 👩‍💻 Developer

**Panchali Samarasinghe**  
University of Kelaniya (MIT) | SLIIT (Software Engineering)

---

## 📝 License

This project is licensed under MIT – for educational and demonstration purposes only.