@echo off
cd backend-api && start gralew build && cd ../
cd job-logger && start gralew build && cd ../
cd web-client && start npm run-script build && cd ../ 
cd text-rank-calc && start gradlew build && cd ../
cd vowel-cons-counter && start gradlew build && cd ../
cd vowel-cons-rater && start gradlew build && cd ../