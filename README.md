# Hospital-Pharmasy

cd back
gradle assemble
docker build -t hospital-pharmacy-service:latest .
cd ..
docker-compose up -d