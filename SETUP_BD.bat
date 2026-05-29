@echo off
echo ============================================
echo   INGENIERIA DEL AHORRO - Crear Base de Datos
echo ============================================
echo.
echo Ingresa tu contrasena de MySQL root:
set /p MYSQL_PASS=Password: 

echo.
echo Creando base de datos...
mysql -u root -p%MYSQL_PASS% < database\schema.sql

if %errorlevel% equ 0 (
    echo.
    echo [OK] Base de datos creada exitosamente!
    echo Base de datos: ingenieria_ahorro
) else (
    echo.
    echo [ERROR] No se pudo crear la base de datos.
    echo Verifica que MySQL este corriendo y la contrasena sea correcta.
)

pause
