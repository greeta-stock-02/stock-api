docker-compose -f docker-app-compose.yml down --remove-orphans
docker-compose down --remove-orphans

docker-compose -f docker-app-compose.yml up -d
docker-compose up -d
