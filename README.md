<h1 align="center"><strong><em>TicketTracker - Kanban Management System</strong></em></h1>

<p align="center">

  <img src="https://cdn-icons-png.flaticon.com/512/17008/17008681.png" alt="kanban-board-logo" height="300" width="300">

</p>



# 📖 Overview

**TicketTracker** is a professional task management application built with **Spring Boot** and **React**. It features a dynamic Kanban board, multi-language support (i18n), and a robust administrative system for project assignments and user management.



# 🚀 How to run the app?



### Backend (Spring Boot)

* Create a MySQL schema named `kanban_db`.

* Update `src/main/resources/application.properties` with your DB username and password.

* Run the application using your IDE or `./mvnw spring-boot:run`.



### Frontend (React)

* Navigate to the frontend folder: `cd frontend`

* Install dependencies: `npm install`

* Start the app: `npm start`



---



<h1 align="center"><strong>Demonstration</strong></h1>



<h2 align="center"><strong>Main Dashboard</strong></h2>

<p align="center">

  <img src="demo/dashboard.png" height="425" width="1000" alt="Dashboard">

  <br>

  <em>Manage your tasks across TODO, In Progress, and DONE columns.</em>

</p>



<h2 align="center"><strong>Admin Permissions & Security</strong></h2>

<p align="center">

  <img src="demo/permissions.png" height="425" width="1000" alt="Permissions">

  <img src="demo/manage_users.png" height="425" width="1000" alt="Manage Users">

  <br>

  <em>Admins can assign users to specific projects and manage system access.</em>

</p>



<h2 align="center"><strong>Ticket Details & User Profile</strong></h2>

<p align="center">

  <img src="demo/ticket_details.png" height="425" width="480" alt="Ticket Details">

  <img src="demo/user_profile.png" height="425" width="480" alt="User Profile">

</p>



---



## ✨ Key Features

* **Full CRUD:** Create, edit, and delete Projects, Boards, and Tickets.

* **Role-Based Access:** Specialized views for Admins, Managers, and Users.

* **Internationalization:** Easily switch between languages (i18n).

* **Profile Management:** Users can update their avatars and view personal information.



## 🛠️ Tech Stack

* **Frontend:** React.js, i18next, Material UI.

* **Backend:** Java, Spring Boot, Spring Security (JWT/Session).

* **Database:** MySQL / Hibernate JPA.
