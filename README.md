# 🚀 Space War – Multiplayer Game (LAN)

Space War là một game bắn tàu vũ trụ multiplayer thời gian thực, được phát triển bằng **Java (Swing + Socket)** theo mô hình **Client – Server**, hỗ trợ kết nối **LAN / IP**, có chat trong game và hệ thống gameplay realtime.

---

## 🎮 TÍNH NĂNG CHÍNH

### 🕹 Gameplay
- Điều khiển tàu vũ trụ di chuyển, bắn đạn và kỹ năng
- Hệ thống máu (HP), kỹ năng có cooldown
- Xử lý va chạm, thắng / thua
- Gameplay realtime nhiều người chơi

### 🌐 Multiplayer (Client – Server)
- Kết nối server qua **LAN Broadcast** hoặc **IP thủ công**
- Server quản lý trạng thái player
- Đồng bộ vị trí, đạn, kỹ năng giữa các client
- Hỗ trợ nhiều client cùng lúc

### 💬 In-game Chat
- Chat realtime giữa các người chơi
- Nhập chat trực tiếp trong lúc chơi
- Giới hạn số ký tự chat
- Tự động xuống dòng khi nội dung dài
- Không tràn khung chat
- Âm thanh thông báo khi có tin nhắn mới

---

## ⌨️ ĐIỀU KHIỂN

### 🎮 Di chuyển
- **W / A / S / D** : Di chuyển tàu
- **Chuột** : Hướng bắn
- **Chuột trái** : Bắn thường
- **Chuột phải** : Kỹ năng đặc biệt

### ⚡ Kỹ năng
- **Q** : Bắn đa đạn
- **X** : Tên lửa
- **SPACE** : Dash

### 💬 Chat
- **T** : Mở chat
- **ENTER** : Gửi chat
- **BACKSPACE** : Xoá ký tự
- **Q** : Huỷ chat

---

## 🧩 KIẾN TRÚC HỆ THỐNG

### 📌 Mô hình


### 📂 Thành phần
- **Game Server**  
  - Quản lý player
  - Đồng bộ gameplay
- **Chat Server**  
  - Gửi / nhận tin nhắn realtime
- **Client**
  - Render game (Swing)
  - Xử lý input
  - Gửi / nhận dữ liệu mạng

---

## 🛠 CÔNG NGHỆ SỬ DỤNG

- **Ngôn ngữ**: Java
- **UI**: Java Swing
- **Network**:
  - TCP Socket (game & chat)
  - UDP Broadcast (tìm server LAN)
- **Âm thanh**: Java Sound API
- **Đa luồng**: Thread

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

