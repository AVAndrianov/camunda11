# Camunda11

## Описание проекта

Приложение для получения и обработки данных с биржи.

## Основные компоненты

- **Backend:** Java, Camunda
- **Database:** H2 

## Требования

- Docker

## Запуск приложения

### Локальный запуск

1. Клонируйте репозиторий:

    ```bash
    git clone https://github.com/AVAndrianov/camunda11.git
    cd camunda11
    ```
   
2. Собирите образ:

   ```bash
   docker build -t camunda/11:v1 .
   ``` 

3. Запустите образ:
  
   ```bash
   docker run -p 8080:8080 camunda/11:v1
   ```  

4. Откройте браузер и перейдите по адресу:

    - Camunda: `http://localhost:8080/camunda`

## API эндпоинты

- **POST /auth/sign-up:** Регистрация нового пользователя.
- **POST /auth/sign-in:** Авторизация пользователя.
- **GET /otc/lookup-table/startProcessUrl** Запускает бп. Данные для обработки будут получены по url: `https://api.spimex.com/otc/lookup-tables/1` в блоке Stock Exchange

