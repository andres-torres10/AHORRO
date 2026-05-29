@echo off
echo ============================================
echo   INGENIERIA DEL AHORRO - Iniciando...
echo ============================================
echo.

echo [1/3] Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java no esta instalado.
    echo Descarga Java 17 en: https://adoptium.net
    pause
    exit /b 1
)

echo.
echo [2/3] Verificando Maven...
mvn -version
if %errorlevel% neq 0 (
    echo ERROR: Maven no esta instalado.
    echo Descarga Maven en: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo.
echo [3/3] Iniciando Spring Boot...
echo El servidor arrancara en: http://localhost:8080
echo Para detenerlo presiona Ctrl+C
echo.
mvn spring-boot:run

pause
