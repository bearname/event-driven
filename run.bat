@echo off
cd backend-api && start run.bat && cd ../
cd text-processing-limiter && start run.bat && cd ../
cd text-rank-calc && start run.bat && cd ../
cd job-logger && start run.bat && cd ../
cd vowel-cons-counter && start run.bat && cd ../
cd vowel-cons-rater && start run.bat && cd ../
cd text-statistics && start run.bat && cd ../
cd frontend && start npm start && cd ../ 