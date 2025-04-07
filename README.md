# 🚀 Flyway Data Migration trong Spring Boot

## ⚡ Flyway là gì?

**Flyway** là một công cụ mã nguồn mở giúp quản lý phiên bản cơ sở dữ liệu (database versioning) và migration. Nó cho phép theo dõi, quản lý, và áp dụng các thay đổi cơ sở dữ liệu một cách có tổ chức và an toàn.

### 🔑 Lợi ích chính khi sử dụng Flyway:

1. **Quản lý phiên bản**: Mỗi thay đổi DB được đánh version rõ ràng
2. **Tính nhất quán**: Đảm bảo schema giống nhau ở mọi môi trường
3. **Tự động hóa**: Tự động áp dụng các thay đổi khi ứng dụng khởi động
4. **Lịch sử thay đổi**: Lưu trữ và theo dõi mọi thay đổi qua bảng FLYWAY_SCHEMA_HISTORY
5. **Hỗ trợ đa DB**: Hỗ trợ nhiều loại cơ sở dữ liệu khác nhau

## 📦 Tích hợp Flyway với Spring Boot

### ⚙️ Thêm Dependencies vào `pom.xml`

```xml
<!-- Flyway Core - Spring Boot đã bao gồm nó trong starter -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- Driver dành riêng cho MySQL -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>

<!-- Để chạy migration từ command line (không bắt buộc) -->
<build>
    <plugins>
        <plugin>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### 🔧 Cấu hình Flyway trong `application.properties`

```properties
# Cấu hình cơ bản
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=0

# Cấu hình nâng cao (tùy chọn)
# spring.flyway.validate-on-migrate=true
# spring.flyway.clean-disabled=true
# spring.flyway.placeholder-replacement=true
```

### 🔧 Cấu hình Flyway trong `application.yml`

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    baseline-version: 0
    # Cấu hình nâng cao (tùy chọn)
    # validate-on-migrate: true
    # clean-disabled: true
    # placeholder-replacement: true
```

## 📝 Cách sử dụng Flyway trong Spring Boot

### 1. Tạo cấu trúc thư mục

Tạo thư mục `src/main/resources/db/migration` để chứa các tập tin migration:

```
src/
└── main/
    └── resources/
        └── db/
            └── migration/
                ├── V1__Create_users_table.sql
                ├── V2__Add_email_column.sql
                └── V3__Insert_default_users.sql
```

### 2. Quy ước đặt tên tập tin migration

Flyway yêu cầu các tập tin migration phải theo định dạng:

```
V{version}__{description}.sql
```

Trong đó:
- `V` là prefix cố định, viết hoa
- `{version}` là số phiên bản, có thể bao gồm dấu chấm (1, 1.1, 2.0.1,...)
- `__` là hai dấu gạch dưới phân cách phiên bản và mô tả
- `{description}` là mô tả ngắn gọn về migration, sử dụng gạch dưới thay cho khoảng trắng
- `.sql` là phần mở rộng của tập tin

### 3. Ví dụ các tập tin migration

**V1__Create_users_table.sql**
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**V2__Add_email_column.sql**
```sql
ALTER TABLE users
ADD COLUMN email VARCHAR(100);
```

**V3__Insert_default_users.sql**
```sql
INSERT INTO users (username, password, email)
VALUES ('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'admin@example.com');
```

### 4. Chạy migration

Spring Boot sẽ tự động chạy Flyway migration khi ứng dụng khởi động. Các migration chỉ được thực thi một lần, theo thứ tự của phiên bản.

### 5. Chạy migration từ command line (sử dụng Flyway Maven Plugin)

```bash
# Hiển thị thông tin về migration đã chạy
mvn flyway:info

# Thực hiện migration
mvn flyway:migrate

# Xóa tất cả các đối tượng trong database (cẩn thận khi sử dụng)
mvn flyway:clean

# Reset lại database và chạy lại tất cả migration
mvn flyway:clean flyway:migrate
```

## 🔍 Kiểm tra trạng thái migration

Flyway tạo và sử dụng bảng `flyway_schema_history` để theo dõi các migration đã được thực thi:

```sql
SELECT * FROM flyway_schema_history;
```

## 🔄 Chiến lược xử lý lỗi

1. **Khi mới triển khai**: Sử dụng `baseline-on-migrate=true` để khởi tạo bảng lịch sử trên database hiện có
2. **Migration thất bại**: Sửa lỗi trong script gây lỗi và chạy lại
3. **Cần rollback**: Flyway Community không hỗ trợ rollback tự động, cần tạo script mới để hoàn tác thay đổi

## 📊 So sánh với các công cụ tương tự

| Tính năng | Flyway | Liquibase |
|-----------|---------|-----------|
| Định dạng | SQL thuần túy | XML, YAML, JSON, SQL |
| Học và sử dụng | Đơn giản | Phức tạp hơn |
| Rollback | Chỉ có trong bản Pro | Có sẵn |
| Tích hợp Spring Boot | Tốt | Tốt |
| Hỗ trợ cộng đồng | Rộng rãi | Rộng rãi |

## 📚 Code mẫu và Tài liệu tham khảo

### 📦 Code mẫu

Các bạn có thể tìm thấy code mẫu chi tiết về cách sử dụng Flyway trong Spring Boot tại các branch của dự án Git này. Mỗi branch sẽ cung cấp các ví dụ khác nhau về cách triển khai và sử dụng Flyway trong các tình huống thực tế.

### 📖 Tài liệu tham khảo:

1. [Hướng dẫn migrate cơ sở dữ liệu sử dụng Flyway trong ứng dụng Spring Boot](https://magz.techover.io/2023/01/30/huong-dan-migrate-co-so-du-lieu-su-dung-flyway-trong-ung-dung-spring-boot/) - Techover Magazine
2. [Flyway Documentation](https://flywaydb.org/documentation/) - Tài liệu chính thức
3. [Spring Boot & Flyway Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway) - Hướng dẫn tích hợp Spring Boot và Flyway
4. [Flyway GitHub Repository](https://github.com/flyway/flyway) - Mã nguồn Flyway

### 🌟 Các bài viết nâng cao:

1. [Database Migrations with Flyway](https://www.baeldung.com/database-migrations-with-flyway) - Baeldung
2. [Spring Boot with Flyway and PostgreSQL](https://www.callicoder.com/spring-boot-flyway-database-migration-example/) - CalliCoder
3. [Using Java-based migrations](https://flywaydb.org/documentation/concepts/migrations#java-based-migrations) - Flyway Documentation
