@echo off
REM Script to properly set up IntelliJ IDEA to recognize generated sources

echo ================================================================
echo  IntelliJ IDEA Setup for Generated Sources
echo ================================================================
echo.

echo Step 1: Generating sources from JSON schemas...
call gradlew.bat generateJsonSchema2Pojo
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to generate sources!
    pause
    exit /b 1
)
echo [OK] Sources generated
echo.

echo Step 2: Updating IntelliJ IDEA module configuration...
call gradlew.bat ideaModule
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to update IDEA configuration!
    pause
    exit /b 1
)
echo [OK] IDEA configuration updated
echo.

echo ================================================================
echo  IMPORTANT - MANUAL STEP REQUIRED IN INTELLIJ IDEA
echo ================================================================
echo.
echo The Gradle build is configured correctly, but IntelliJ IDEA
echo needs to be refreshed to recognize the generated sources.
echo.
echo Please do ONE of the following in IntelliJ IDEA:
echo.
echo   OPTION 1 (Fastest):
echo   ------------------------
echo   Press: Ctrl + Shift + O
echo.
echo   OPTION 2 (Menu):
echo   ------------------------
echo   1. Right-click on project root
echo   2. Select: Gradle ^> Refresh Gradle Project
echo.
echo   OPTION 3 (Gradle Tool Window):
echo   ------------------------
echo   1. Open: View ^> Tool Windows ^> Gradle
echo   2. Click the Refresh button (circular arrows icon)
echo.
echo ================================================================
echo.
echo After refreshing, verify:
echo   - build/generated-sources/jsonschema2pojo/ has a BLUE icon
echo   - Opening JsonInternalSoknad.java shows NO warnings
echo   - Code completion works in the generated files
echo.
echo ================================================================
echo.

pause

