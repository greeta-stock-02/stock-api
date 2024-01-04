docker-compose -f docker-app-compose.yml down
docker-compose down
docker-compose up -d

mvn clean install -DskipTests

cd ./user-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=user-service

cd ../product-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=product-service

cd ../payment-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=payment-service

cd ../order-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=order-service

cd ../customer-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=customer-service

cd ../restaurant-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=restaurant-service

cd ../food-ordering-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=food-ordering-service

cd ../gateway-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=gateway-service

cd ../

docker-compose -f docker-app-compose.yml up -d