# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

專案目標 **Java 17**。`.java-version` 已設為 `17`，若使用 **jenv** 可直接執行下列指令：

```bash
# 啟動專案
mvn spring-boot:run

# 只編譯
mvn compile

# 打包
mvn package -DskipTests
```

若系統預設 JDK 非 17（例如 Java 25/26），請加上 `JAVA_HOME` 前綴：

```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@17 mvn spring-boot:run
```

啟動後 API base URL 為 `http://localhost:8080/api`。

## Architecture

**Spring Boot 2.7.18 / Java 17 / MyBatis / MySQL**

請求流程：
```
Request → JwtFilter → Controller → Service → Mapper (XML) → MySQL
```

**JWT 認證**：`JwtFilter` 攔截所有請求，僅放行 `/auth/login`、`/auth/register` 與 `/api/files/**`。Token 放在 `Authorization: Bearer <token>` header。

**回應格式**：所有 API 統一回傳 `Result<T>`（`code`, `message`, `data`），由 `GlobalExceptionHandler` 統一處理例外並包成相同格式。

**模組職責**：
- `entity/` — 資料庫對應物件，全用 Lombok `@Data`
- `dto/` — 請求入參，附 Bean Validation 注解
- `vo/Result` — 統一回應包裝器
- `mapper/` — MyBatis 介面 + `resources/mapper/*.xml`（含 resultMap 做一對多組裝）
- `aspect/LogAspect` — AOP 記錄所有 controller 的入參與執行耗時

**Portfolio 一對多**：`Portfolio` 透過 MyBatis `<collection>` lazy select 組裝 `PortfolioImage` 列表，對應表為 `portfolios` + `portfolio_images`。

**檔案上傳**：存放於本地 `./uploads/`，透過 `WebConfig` 映射為靜態資源，對外路徑為 `/api/files/{filename}`，此路徑不需 JWT。

## 環境需求

- MySQL：`localhost:3306`，資料庫名 `pone_website`，帳號 `root`
- 密碼與 JWT secret 直接寫在 `application.yml`（開發環境）

## IDE 設定（IntelliJ IDEA）

- `.idea/misc.xml`：Project SDK 設定為 `JDK_17 / openjdk-17`
- `.idea/compiler.xml`：已建立 Lombok annotation processing profile，指向 `lombok-1.18.38.jar`；Javac 啟用 `-parameters`
- `pom.xml`：`maven-compiler-plugin 3.11.0` 顯式宣告 `annotationProcessorPaths`，確保 Lombok 在任何環境均可正確處理 annotation
