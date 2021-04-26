@echo off
docker ps -q --filter ancestor=mikhailmi/ds-backend-api:%1 > out
SET /p containerId= < out
echo %containerId%
docker stop %containerId%