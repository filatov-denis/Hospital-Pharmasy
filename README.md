# Hospital-Pharmacy

Находясь в консоли проекта

cd back
gradle assemble
docker build -t hospital-backend:latest .
cd ../front
docker build -t hospital-frontend:latest .
cd ..
docker-compose up -d