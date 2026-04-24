"- Hệ thống Tài khoản & Bảo mật: Đăng nhập bằng tài khoản nội bộ; mã hóa mật khẩu bằng BCrypt; hỗ trợ ghi nhớ và duy trì trạng thái đăng nhập; tự động chặn đăng nhập đối với tài khoản bị khóa hoặc vô hiệu hóa.

- Giao diện Responsive, hỗ trợ Dark Mode / Light Mode: Đảm bảo hiển thị tốt trên nhiều thiết bị và nâng cao trải nghiệm người dùng.
- Phân quyền động theo vai trò/chức vụ: Người dùng được cấp quyền truy cập và sử dụng chức năng tương ứng với chức vụ được gán trong CLB.
- Quản lý mật khẩu: Yêu cầu người dùng thay đổi mật khẩu khi đăng nhập lần đầu nhằm tăng cường bảo mật.
- Quản lý học kỳ: Xem thông tin học kỳ hiện tại và cho phép thay đổi học kỳ áp dụng trong hệ thống.
- Theo dõi hoạt động CLB: Xem thống kê hoạt động của CLB và nhận các thông báo do CLB phát hành.
- Công việc và sự kiện: Xem danh sách sự kiện và công việc được phân công; gửi yêu cầu đánh dấu tham gia công việc kèm mô tả/ghi chú; theo dõi trạng thái xử lý yêu cầu." "- Hệ thống Tài khoản & Bảo mật: Đăng nhập bằng tài khoản nội bộ; mã hóa mật khẩu bằng BCrypt; hỗ trợ ghi nhớ và duy trì trạng thái đăng nhập; tự động chặn đăng nhập đối với tài khoản bị khóa hoặc vô hiệu hóa.
- Giao diện Responsive, hỗ trợ Dark Mode / Light Mode: Đảm bảo hiển thị tốt trên nhiều thiết bị và nâng cao trải nghiệm người dùng.
- Phân quyền động theo vai trò/chức vụ: Người dùng được cấp quyền truy cập và sử dụng các chức năng tương ứng với chức vụ được gán trong CLB; backend kiểm soát quyền truy cập và frontend chỉ hiển thị các chức năng phù hợp.
- Quản lý học kỳ: Cho phép xem thông tin học kỳ hiện tại và thay đổi học kỳ đang được áp dụng trong hệ thống.
- Quản lý người dùng: Xem danh sách người dùng; cấp tài khoản, khóa/mở khóa tài khoản; gán chức vụ cho người dùng trong CLB.
- Quản lý chức vụ: Tạo, chỉnh sửa và xóa các chức vụ; thiết lập các quyền tương ứng với từng chức vụ để kiểm soát truy cập các chức năng hệ thống.
- Quản lý thành viên: Quản lý thông tin thành viên CLB gắn với tài khoản người dùng; hỗ trợ thiết lập và cập nhật ảnh đại diện.
- Quản lý kỳ học: Thực hiện thêm, sửa, xóa các kỳ học; quản lý danh sách thành viên tham gia trong từng kỳ học.
- Quản lý công việc: Tạo, chỉnh sửa và xóa các công việc/sự kiện của CLB.
- Kiểm tra và duyệt công việc: Xem danh sách các yêu cầu tham gia công việc; thực hiện duyệt hoặc từ chối yêu cầu kèm theo phản hồi." "- Backend:

* Java 21
* Spring boot 3.5.6
* Spring secutiry
* Gradle
* Json web token
* Swagger ui
* Lombok
* ModelMapper
* Sping JPA
* Flyway mysql
* Cloudinary
* Spring validation
* Spotless
* Docker

- Database:

* Mysql 8.4

- Frontend:

* Typescript
* Next.js ^16
* Antd ^6
* Tanstack query
* Tailwind 4
* Apexcharts
* Framer motion
* Hero UI
* Zod
* Nuqs
* Husky
* Prettier
* Nextjs theme
* Eslint" "- Phân quyền động theo vai trò/chức vụ: Người dùng được cấp quyền truy cập và sử dụng các chức năng tương ứng với chức vụ được gán trong CLB; backend kiểm soát chặt chẽ quyền truy cập, frontend chỉ hiển thị các chức năng phù hợp với quyền của người dùng.

- Lưu trữ hình ảnh trên Cloudinary: Ảnh được lưu trữ trên nền tảng cloud, giúp giảm tải cho backend và tăng hiệu năng hệ thống.
- Hỗ trợ Refresh Token và đăng nhập đa thiết bị: Cho phép người dùng đăng nhập trên nhiều thiết bị cùng lúc và duy trì phiên đăng nhập an toàn.
- Giao diện Responsive, hỗ trợ Dark Mode / Light Mode: Đảm bảo hiển thị tốt trên nhiều thiết bị và nâng cao trải nghiệm người dùng.
- Spring Validation: Đảm bảo dữ liệu đầu vào luôn hợp lệ, nhất quán và giảm thiểu lỗi trong quá trình xử lý nghiệp vụ.
- Chuẩn hóa mã nguồn: Sử dụng Spotless (backend) và Prettier (frontend) để format code đồng nhất, nâng cao chất lượng source code và khả năng bảo trì.
- Swagger UI: Tích hợp Swagger UI để tài liệu hóa và kiểm thử API tại địa chỉ http://localhost:8080/swagger-ui/index.html."
