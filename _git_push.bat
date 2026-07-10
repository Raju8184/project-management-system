@echo off
cd /d "%~dp0"
taskkill /F /IM git.exe 2>nul
timeout /t 1 /nobreak >nul
del .git\index.lock 2>nul
git add -A > git_out.txt 2>&1
git commit -m "feat: premium web app redesign and Swing Dashboard overhaul" >> git_out.txt 2>&1
git push origin main >> git_out.txt 2>&1
echo DONE >> git_out.txt
