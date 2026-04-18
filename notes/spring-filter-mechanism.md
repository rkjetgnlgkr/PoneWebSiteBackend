# Spring Filter 機制筆記 — JwtFilter 為例

## JwtFilter.doFilterInternal 的執行時機

**每一個 HTTP 請求進來時都會執行**，在到達 Controller 之前。

---

## 呼叫鏈（由外到內）

```
HTTP Request
    ↓
Servlet Container (Tomcat)
    ↓
CorsFilter（先處理跨域）
    ↓
JwtFilter.doFilterInternal  ← 在這裡驗證 Token
    ↓
DispatcherServlet
    ↓
Controller
```

---

## 機制原理

### 1. 繼承 `OncePerRequestFilter`

```java
public class JwtFilter extends OncePerRequestFilter
```

- `OncePerRequestFilter` 是 Spring 提供的抽象類別
- 保證同一個請求只執行一次（避免 forward/include 時重複觸發）
- 底層實作了 Java Servlet 規範的 `javax.servlet.Filter` 介面

### 2. Spring 自動註冊（`@Component`）

```java
@Component  // ← 關鍵
public class JwtFilter extends OncePerRequestFilter
```

- 加上 `@Component` 後，Spring Boot 啟動時自動偵測所有實作 `Filter` 的 Bean
- 透過內部的 `FilterRegistrationBean` 將它註冊到 Tomcat 的 Filter Chain 中
- 不需要任何額外設定，Spring Boot 自動完成

### 3. Filter Chain（放行 vs 攔截）

```java
filterChain.doFilter(request, response);  // 放行，交給下一個 Filter 或 Controller
```

Tomcat 維護一條 Filter Chain，每個 Filter 有兩個選擇：

| 動作 | 方式 | 結果 |
|------|------|------|
| 放行 | 呼叫 `filterChain.doFilter()` | 繼續往下走，最終到達 Controller |
| 攔截 | 直接寫 response，不呼叫 doFilter | 請求到此為止，不會進 Controller |

---

## JwtFilter 完整流程圖

```
Tomcat 收到請求
    │
    ▼
JwtFilter.doFilterInternal()
    │
    ├─ 路徑是 /auth/login 或 /api/files/**？
    │       ↓ Yes
    │   filterChain.doFilter() → 直接放行（不需驗證）
    │
    ├─ 有 Authorization: Bearer <token> header？
    │       ↓ No
    │   writeUnauthorized(401) → 結束，不進 Controller
    │
    ├─ jwtUtil.validateToken(token) 驗證通過？
    │       ↓ No
    │   writeUnauthorized(401) → 結束，不進 Controller
    │
    └─ 全部通過
        filterChain.doFilter() → 繼續到 Controller
```

---

## 本專案的放行白名單

```java
// JwtFilter.java
if (path.contains("/auth/login") || path.startsWith("/api/files/")) {
    filterChain.doFilter(request, response);
    return;
}
```

- `/auth/login` — 登入不需要 Token（還沒有 Token）
- `/api/files/**` — 靜態圖片公開存取，不需驗證

---

## 關鍵類別關係

```
javax.servlet.Filter          ← Java Servlet 規範（介面）
    ↑
GenericFilterBean             ← Spring 封裝（抽象類別）
    ↑
OncePerRequestFilter          ← 保證每請求只執行一次（抽象類別）
    ↑
JwtFilter                     ← 本專案實作，覆寫 doFilterInternal()
```
