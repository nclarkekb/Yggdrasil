@echo off
setlocal enableextensions

call %~dp0\env.cmd

if "%JAVA_OPTS%" == "" (
   set JAVA_OPTS=-Xms256m -Xmx1024m -XX:PermSize=64M -XX:MaxPermSize=256M
)

REM %JAVA% %JAVA_OPTS% -cp "%CP%" ${assembly.main.class.name} %*
%JAVA% %JAVA_OPTS% -cp "%CP%" dk.kb.yggdrasil.xslt.XmlValidate %*
