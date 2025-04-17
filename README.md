# Banking App

## Технологии

- Java
- Spring Boot
- PostgreSQL
- JPA (Hibernate)
- Flyway
- Testcontainers (для интеграционных тестов)
- Swagger (OpenAPI UI)
- Docker & Docker Compose

---
## Скачивание

Клонируй репозиторий:

```bash
git clone https://github.com/aidarbekq/banking-app.git
cd banking-app
````
##  Запуск с Docker Compose

###  Требования:
- Docker
- Docker Compose

###  Сборка и запуск приложения

```bash
# 1. Собери JAR-файл
./mvnw clean package -DskipTests

# 2. Запусти сервисы
docker-compose up --build
```
##  Доступ к приложению

Приложение будет доступно по адресу:  
➡️ [http://localhost:8080](http://localhost:8080)

База данных PostgreSQL будет работать на порту:  
➡️ `localhost:5432`, база данных: `banking_db`, логин/пароль: `postgres`

---

##  API Документация

Swagger UI доступен по адресу:  
➡️ [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## SQL-запросы

Файл `queries.sql` содержит пример SQL-запросов:

- Получение пользователей с общим балансом > 10 000
- Поиск пользователя по email
- Суммарный баланс всех счетов

---

## Тестирование

Запуск юнит и интеграционных тестов:

```bash
./mvnw test
```

##  Переменные окружения

(Можно настроить в `docker-compose.yml`):

| Переменная                   | Значение по умолчанию                           |
|-----------------------------|-------------------------------------------------|
| `SPRING_DATASOURCE_URL`     | `jdbc:postgresql://postgres:5432/banking_db`   |
| `SPRING_DATASOURCE_USERNAME`| `postgres`                                      |
| `SPRING_DATASOURCE_PASSWORD`| `postgres`                                      |

---

## Очистка

Остановить контейнеры:

```bash
docker-compose down
```

Удалить volume с данными:

```bash
docker-compose down -v
```