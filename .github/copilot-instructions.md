# Project: oshibi（お笑いライブスケジュール管理アプリ）

## Tech Stack
- Java 17, Spring Boot 3, Thymeleaf, PostgreSQL
- Spring Data JPA / Hibernate
- Spring Security (BCrypt, role-based auth)
- Lombok (use @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- Flyway for DB migration
- Gradle build system

## Coding Conventions
- Entity classes use Lombok annotations, not manual getters/setters
- Service layer handles all business logic (Controllers are thin)
- Repository layer uses Spring Data JPA interfaces
- Japanese comments are OK for domain-specific terms
- Use Java 17 features (text blocks, switch expressions) where appropriate