# 🚚 Logistic Operation System

## 📌 Overview
A backend-based logistics management system designed to handle end-to-end shipment operations including booking, route planning, driver and vehicle assignment, billing, and invoicing.

The system centralizes logistics processes under admin control, improving operational efficiency and reducing manual work.

---

## 🛠 Tech Stack
- Java  
- Spring Boot  
- Spring Security  
- MySQL  
- REST APIs  
- JWT Authentication  

---
## 🧱 Project Structure

src/main/java/com/example/project
│
├── controller/ # Handles HTTP requests and APIs
├── service/ # Business logic implementation
├── repository/ # Database interaction (JPA repositories)
├── entity/ # Database entities / models
├── dto/ # Data Transfer Objects
├── config/ # Security and application configuration
├── security/ # JWT authentication and filters
├── util/ # Utility classes

---

## 👥 Key Roles

### 🔹 Admin
- Manages complete logistics operations  
- Assigns drivers, vehicles, and routes  
- Handles booking, billing, and system monitoring  

### 🔹 Consignor (Sender)
- The person or business that sends goods  
- Provides shipment details such as source, destination, and package info  

### 🔹 Consignee (Receiver)
- The person or business that receives goods  
- Accepts delivery at the destination  

---

## 🔥 Core Features
- Shipment booking and scheduling  
- Route management and vehicle assignment  
- Driver allocation for shipments  
- LR (Lorry Receipt) generation  
- Invoice generation and billing system  
- JWT-based authentication and secure access  
- Automated notifications (WhatsApp and Email)  

---

## 📦 Modules
- User Management (Consignor / Consignee / Admin)  
- Booking & Shipment Management  
- Route & Vehicle Management  
- Driver Assignment  
- Billing & Invoice Generation  
- LR Receipt Management  

---

## ⚙️ Workflow
1. Admin creates or manages shipment booking  
2. Consignor and consignee details are recorded  
3. Route, vehicle, and driver are assigned  
4. LR receipt is generated for shipment  
5. Goods are transported to destination  
6. Invoice is generated and billing is completed  

---

## ▶️ How to Run
1. Clone the repository  
   ```bash
   git clone https://github.com/BilalMirje/Sudarshan_Logistics

2. Configure MySQL database
3. Update application.properties
4. Run Spring Boot application

📈 Impact
Reduced manual paperwork by 80%
Improved billing accuracy and operational efficiency

👨‍💻 Developer
Bilal Mirje
