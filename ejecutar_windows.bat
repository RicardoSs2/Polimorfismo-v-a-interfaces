@echo off
setlocal EnableExtensions EnableDelayedExpansion
chcp 65001 >nul
cd /d "%~dp0"
if not exist "out" mkdir "out"
>"fuentes.txt" (
    for /r "src" %%f in (*.java) do (
        set "archivo=%%~ff"
        set "archivo=!archivo:\=/!"
        echo "!archivo!"
    )
)
javac -encoding UTF-8 -d "out" @fuentes.txt
if errorlevel 1 (
    del "fuentes.txt" 2>nul
    echo Error al compilar el proyecto.
    pause
    exit /b 1
)
del "fuentes.txt" 2>nul
if /i "%~1"=="--gui" (
    java -cp "out" tienda.Main --gui
) else if /i "%~1"=="--test" (
    java -cp "out" tienda.pruebas.Pruebas
) else (
    java -cp "out" tienda.Main
)
pause
