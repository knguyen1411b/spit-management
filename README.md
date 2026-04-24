# CLB Management System

## Giới thiệu

CLB Management System là hệ thống quản lý hoạt động câu lạc bộ, hỗ trợ quản lý người dùng, thành viên, học kỳ, công việc và sự kiện. Hệ thống được xây dựng theo mô hình fullstack với frontend và backend tách biệt, áp dụng các công nghệ hiện đại nhằm đảm bảo bảo mật, khả năng mở rộng và trải nghiệm người dùng tốt.

---

## Chức năng dành cho người dùng

- **Hệ thống Tài khoản & Bảo mật**  
  Đăng nhập bằng tài khoản nội bộ; mật khẩu được mã hóa bằng BCrypt; hỗ trợ ghi nhớ và duy trì trạng thái đăng nhập; tự động chặn đăng nhập đối với các tài khoản bị khóa hoặc vô hiệu hóa.

- **Giao diện Responsive, Dark Mode / Light Mode**  
  Giao diện thân thiện, hiển thị tốt trên nhiều thiết bị và hỗ trợ chế độ sáng/tối nhằm nâng cao trải nghiệm người dùng.

- **Phân quyền động theo vai trò/chức vụ**  
  Người dùng được truy cập và sử dụng các chức năng phù hợp với chức vụ được gán trong CLB.

- **Quản lý mật khẩu**  
  Yêu cầu người dùng thay đổi mật khẩu khi đăng nhập lần đầu để tăng cường bảo mật.

- **Quản lý học kỳ cá nhân**  
  Xem thông tin học kỳ hiện tại và thay đổi học kỳ đang được áp dụng trong hệ thống.

- **Theo dõi hoạt động CLB**  
  Xem thống kê hoạt động của CLB và nhận các thông báo do CLB phát hành.

- **Công việc và sự kiện**  
  Xem danh sách công việc và sự kiện được phân công; gửi yêu cầu đánh dấu tham gia kèm mô tả/ghi chú; theo dõi trạng thái xử lý yêu cầu.

---

## Chức năng quản trị (Admin / Ban điều hành CLB)

- **Phân quyền động theo vai trò/chức vụ**  
  Backend kiểm soát chặt chẽ quyền truy cập, frontend chỉ hiển thị các chức năng tương ứng với quyền của người dùng.

- **Quản lý người dùng**  
  Xem danh sách người dùng; cấp tài khoản; khóa hoặc mở khóa tài khoản; gán chức vụ cho người dùng trong CLB.

- **Quản lý chức vụ**  
  Tạo, chỉnh sửa và xóa các chức vụ; thiết lập quyền tương ứng với từng chức vụ để kiểm soát truy cập các chức năng hệ thống.

- **Quản lý thành viên CLB**  
  Quản lý thông tin thành viên gắn với tài khoản người dùng; hỗ trợ thiết lập và cập nhật ảnh đại diện.

- **Quản lý kỳ học**  
  Thêm, sửa, xóa các kỳ học; quản lý danh sách thành viên tham gia trong từng kỳ học.

- **Quản lý công việc và sự kiện**  
  Tạo, chỉnh sửa và xóa các công việc/sự kiện của CLB.

- **Kiểm tra và duyệt công việc**  
  Xem danh sách các yêu cầu tham gia công việc; thực hiện duyệt hoặc từ chối yêu cầu kèm phản hồi.

---

## Ưu điểm và đặc điểm nổi bật

- **Phân quyền động theo vai trò/chức vụ**  
  Quyền truy cập được kiểm soát tại backend và đồng bộ với giao diện frontend, đảm bảo an toàn và trải nghiệm nhất quán.

- **Lưu trữ hình ảnh trên Cloudinary**  
  Ảnh đại diện và tài nguyên hình ảnh được lưu trữ trên nền tảng cloud, giúp giảm tải cho backend và tăng hiệu năng hệ thống.

- **Hỗ trợ Refresh Token và đăng nhập đa thiết bị**  
  Cho phép người dùng đăng nhập đồng thời trên nhiều thiết bị và duy trì phiên đăng nhập an toàn.

- **Đảm bảo tính hợp lệ dữ liệu**  
  Sử dụng Spring Validation để kiểm tra dữ liệu đầu vào, đảm bảo tính nhất quán và giảm thiểu lỗi nghiệp vụ.

- **Chuẩn hóa mã nguồn**  
  Áp dụng Spotless cho backend và Prettier cho frontend nhằm đảm bảo format code đồng nhất và tăng khả năng bảo trì.

- **Swagger UI**  
  Tích hợp Swagger UI để tài liệu hóa và kiểm thử API, hỗ trợ quá trình phát triển và kiểm thử hệ thống.

---

## Công nghệ sử dụng

### Backend

- Java 21
- Spring Boot 3.5.6
- Spring Security
- JSON Web Token (JWT)
- Spring Data JPA
- Flyway (MySQL)
- Spring Validation
- Lombok
- ModelMapper
- Cloudinary
- Swagger UI
- Spotless
- Docker

### Database

- MySQL 8.4

### Frontend

- TypeScript
- Next.js ^16
- Ant Design ^6
- TanStack Query
- Tailwind CSS 4
- ApexCharts
- Framer Motion
- Hero UI
- Zod
- Nuqs
- Husky
- Prettier
- Next.js Theme
- ESLint
