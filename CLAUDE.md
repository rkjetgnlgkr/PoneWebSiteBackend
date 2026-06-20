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

**JWT 認證**：`JwtFilter` 攔截所有請求，僅放行 `/auth/login`、`/auth/register`、`/auth/google`、`/auth/line` 與 `/api/files/**`。Token 放在 `Authorization: Bearer <token>` header。

**Google OAuth 登入**：`POST /auth/google` 接收前端傳入的 Google ID Token，呼叫 `https://oauth2.googleapis.com/tokeninfo` 驗證後，依 `google_id` 查詢或自動建立 User（username 取 email @ 前半段），最終回傳本系統 JWT。`users` 表有 `google_id VARCHAR(255) UNIQUE` 欄位。

**LINE 登入**：`POST /auth/line` 接收前端 OAuth 回呼帶回的 `code` 與 `redirectUri`（`LineLoginDto`，兩者皆 `@NotBlank`）。後端先向 `https://api.line.me/oauth2/v2.1/token` 以 authorization code 換取 access token，再以該 token 向 `https://api.line.me/v2/profile` 取得用戶 profile，依 `line_id`（profile 的 `userId`）查詢或自動建立 User，最終回傳本系統 JWT。Channel 憑證由環境變數 `LINE_CHANNEL_ID` / `LINE_CHANNEL_SECRET` 設定（`application.yml` 的 `line.channel-id` / `line.channel-secret`）。`users` 表需有 `line_id` 欄位，`User.java` 已含 `lineId`。

**回應格式**：所有 API 統一回傳 `Result<T>`（`code`, `message`, `data`），由 `GlobalExceptionHandler` 統一處理例外並包成相同格式。

**模組職責**：
- `entity/` — 資料庫對應物件，全用 Lombok `@Data`
- `dto/` — 請求入參，附 Bean Validation 注解
- `vo/Result` — 統一回應包裝器
- `mapper/` — MyBatis 介面 + `resources/mapper/*.xml`（含 resultMap 做一對多組裝）
- `aspect/LogAspect` — AOP 記錄所有 controller 的入參與執行耗時

**Portfolio 一對多**：`Portfolio` 透過 MyBatis `<collection>` lazy select 組裝 `PortfolioImage` 列表，對應表為 `portfolios` + `portfolio_images`。

**檔案上傳**：存放於本地 `./uploads/`，透過 `WebConfig` 映射為靜態資源，對外路徑為 `/api/files/{filename}`，此路徑不需 JWT。

**版面設定**：`LayoutConfigController`（`/settings`）讀寫 `layout_config` 表。`GET /settings` 查無記錄時回傳預設值 `dark_star`（不寫 DB）；`PUT /settings` 執行 upsert。`LayoutConfigDto` 有 `@Pattern` 限制合法值：`dark_star | nature | terminal`。

**個人資料**：`UserProfileController`（`/profile`）
- `GET /profile`：讀取登入使用者的完整 User 資料（含 `title`, `bio`, `avatar`, `location`）
- `POST /profile/avatar`：上傳大頭貼圖片（multipart），呼叫 `FileService` 上傳後更新 `users.avatar`，回傳圖片路徑

**技能管理**：`SkillController`（`/skills`）對登入使用者（request attribute `userId`）的 `skills` 表做 CRUD：`GET /skills`（列表）、`POST /skills`（新增）、`PUT /skills/{id}`（更新）、`DELETE /skills/{id}`（刪除）。入參為 `SkillDto`（`@Validated`）。

**工作經歷管理**：`WorkExperienceController`（`/work-experiences`）對登入使用者的 `work_experiences` 表做 CRUD：`GET /work-experiences`、`POST /work-experiences`、`PUT /work-experiences/{id}`、`DELETE /work-experiences/{id}`。入參為 `WorkExperienceDto`（`@Validated`）。

**User 實體欄位**：`title`, `bio`(TEXT), `avatar`, `location` 已加入 `User.java` 與 `UserMapper.xml`，`findByUsername` / `findByGoogleId` / `findById` 查詢皆含此四欄位。

**檔案 URL 前綴**：`application.yml` 新增環境變數 `UPLOAD_URL_PREFIX`（預設 `/api/files/`）。生產環境可設為 `https://<admin-backend-domain>/api/files/`，讓 DB 存完整絕對 URL，前台直接使用無需再拼接。

## 環境需求

- MySQL：`localhost:3306`，資料庫名 `pone_website`，帳號 `root`
- 密碼與 JWT secret 直接寫在 `application.yml`（開發環境）
- LINE 登入：`LINE_CHANNEL_ID` / `LINE_CHANNEL_SECRET`（需在 LINE Developers Console 建立 Login channel；未設定時 `application.yml` 預設為空字串）

## IDE 設定（IntelliJ IDEA）

- `.idea/misc.xml`：Project SDK 設定為 `JDK_17 / openjdk-17`
- `.idea/compiler.xml`：已建立 Lombok annotation processing profile，指向 `lombok-1.18.38.jar`；Javac 啟用 `-parameters`
- `pom.xml`：`maven-compiler-plugin 3.11.0` 顯式宣告 `annotationProcessorPaths`，確保 Lombok 在任何環境均可正確處理 annotation
