@echo off
echo Starting Happy Ending API in LOCAL mode...
set SPRING_PROFILES_ACTIVE=local
set SERVER_PORT=8085
set OPENAPI_TITLE=Happy Ending API (Local)
set OPENAPI_DESCRIPTION=API documentation for Happy Ending project - Local Development
set OPENAPI_SERVER_DEV_URL=http://localhost:8085
set OPENAPI_SERVER_PROD_URL=http://localhost:8085

echo Environment variables set for LOCAL development
echo Profile: %SPRING_PROFILES_ACTIVE%
echo Port: %SERVER_PORT%
echo.

mvnw spring-boot:run
