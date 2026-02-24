@echo off
echo Starting Happy Ending API in PRODUCTION mode...
set SPRING_PROFILES_ACTIVE=prod
set SERVER_PORT=8080
set OPENAPI_TITLE=Happy Ending API
set OPENAPI_DESCRIPTION=API documentation for Happy Ending project
set OPENAPI_SERVER_DEV_URL=https://dev-api.happyending.com
set OPENAPI_SERVER_PROD_URL=https://api.happyending.com

echo Environment variables set for PRODUCTION
echo Profile: %SPRING_PROFILES_ACTIVE%
echo Port: %SERVER_PORT%
echo.

mvnw spring-boot:run
