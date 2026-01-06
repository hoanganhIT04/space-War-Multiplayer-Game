# 🚀 Space War – Multiplayer Game (Java Socket, LAN)

## 1. Giới thiệu

**Space War** là một game bắn tàu vũ trụ nhiều người chơi (multiplayer) được xây dựng bằng **Java Swing** kết hợp **Java Socket**, triển khai theo mô hình **Client – Server** trong môi trường mạng **LAN**.

Game cho phép nhiều người chơi kết nối vào cùng một phòng chơi, đồng bộ trạng thái theo **thời gian thực**, đồng thời hỗ trợ **chat trực tiếp trong game** nhằm tăng tính tương tác giữa các người chơi.

Dự án được thực hiện với mục tiêu học tập và nghiên cứu các kỹ thuật:
- Lập trình mạng
- Xử lý đa luồng
- Đồng bộ dữ liệu thời gian thực
- Xây dựng game multiplayer cơ bản

---

## 2. Tính năng chính

### 🕹 Gameplay
- Điều khiển tàu vũ trụ di chuyển, bắn đạn và sử dụng kỹ năng
- Hệ thống máu (HP) và kỹ năng có thời gian hồi (cooldown)
- Xử lý va chạm và trạng thái thắng / thua
- Gameplay realtime nhiều người chơi

### 🌐 Multiplayer (Client – Server)
- Kết nối server qua **LAN Broadcast** hoặc **IP thủ công**
- Server quản lý trạng thái người chơi
- Đồng bộ vị trí, hành động, đạn và kỹ năng
- Hỗ trợ nhiều client cùng lúc

### 💬 In-game Chat
- Chat realtime giữa các người chơi
- Nhập chat trực tiếp trong lúc chơi
- Giới hạn số ký tự chat
- Tự động xuống dòng khi nội dung dài
- Không tràn khung chat
- Âm thanh thông báo khi có tin nhắn mới

---

## 3. Điều khiển trong game

### 🎮 Di chuyển
- **W / A / S / D** : Di chuyển tàu
- **Chuột** : Điều hướng và ngắm bắn
- **Chuột trái** : Bắn thường
- **Chuột phải** : Kỹ năng đặc biệt

### ⚡ Kỹ năng
- **Q** : Bắn đa đạn
- **X** : Tên lửa
- **SPACE** : Dash

### 💬 Chat
- **T** : Mở chat
- **ENTER** : Gửi tin nhắn
- **BACKSPACE** : Xoá ký tự
- **Q** : Huỷ chat

---

## 4. Công nghệ sử dụng

### Ngôn ngữ & nền tảng
- **Java (JDK 21)**
- **Java Swing** – xây dựng giao diện đồ họa
- **Java Socket (TCP)** – giao tiếp mạng Client – Server

### Kỹ thuật chính
- Mô hình **Client – Server**
- TCP Socket trong mạng LAN
- Đa luồng (Multithreading)
- Đồng bộ trạng thái game theo thời gian thực
- Broadcast dữ liệu và tin nhắn chat
- Xử lý âm thanh với `javax.sound.sampled`

---

## 5. Kiến trúc hệ thống

Hệ thống bao gồm **2 Server độc lập**:

### 🖥 Game Server
- Quản lý kết nối người chơi
- Nhận và broadcast dữ liệu trạng thái game
- Đồng bộ vị trí, hành động và kỹ năng của người chơi

### 💬 Chat Server
- Xử lý riêng chức năng chat
- Broadcast tin nhắn chat đến toàn bộ client
- Không ảnh hưởng đến luồng dữ liệu gameplay

### 🎮 Client
- Render game bằng Java Swing
- Xử lý input từ người chơi
- Gửi / nhận dữ liệu game và chat từ server

---

## 6. Môi trường kết nối

- Các máy **Client và Server phải cùng mạng LAN / cùng WiFi**
- Kết nối thông qua **IPv4 Address + Port**

### Thông số kết nối
- **IPv4 Address**: IP của máy chạy Server  
  (ví dụ: `192.168.100.121`)
- **Port Game Server**: `4004`
- **Port Chat Server**: `4005`

---

## 7. Hướng dẫn chạy project

⚠️ **Lưu ý:** Hệ thống sử dụng mô hình Client – Server, vì vậy **bắt buộc phải chạy Server trước Client**.

---


## ▶️ HƯỚNG DẪN CHẠY PROJECT

⚠️ **Lưu ý:** Hệ thống sử dụng mô hình Client – Server, vì vậy **bắt buộc phải chạy Server trước Client**.

---

### 1️⃣ Chạy Chat Server 

Chat Server chịu trách nhiệm xử lý chat realtime giữa các client.

```bash
Run file ChatServer.java
```
### 2️⃣ Chạy Game Server

Game Server quản lý gameplay và đồng bộ trạng thái người chơi.

```bash
Run file ServerWindow.java
```
### 3️⃣ Chạy Client

Sau khi Chat Server và Game Server đã chạy thành công:

```bash
Run file ServerWindow.java

