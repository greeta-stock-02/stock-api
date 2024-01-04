### 📖 Full-Stack Templates For Spring Boot Developers

#### ✅ Stock Tracking Swagger UI with Axon Event Sourcing, CQRS and Saga Transactions
#### ✅ Food Ordering UI with Axon Event Sourcing, CQRS and Spring Security Websockets

<ul style="list-style-type:disc">
    <li>📖 This <b>Full-Stack Developer Template</b> provides fully functional Development Environment:</li>
    <li>📖 <b>Event-Driven Spring Boot Microservices</b> with Axon Event Sourcing and Saga Framework</li>
    <li>📖 <b>Swagger UI Gateway</b> with Keycloak Authorization</li>
    <li>📖 <b>Simple jQuery UI</b> with Keycloak Authorization and Secured Websockets</li>
    <li>📖 Local <b>Docker</b> Development Environment</li>
  <li>📖 Full <b>Technology Stack</b>:</li>
  <ul>
    <li>✅ <b>Swagger UI</b></li>
    <li>✅ <b>jQuery UI</b></li>
    <li>✅ <b>Spring Boot</b></li>
    <li>✅ <b>Spring Cloud Gateway</b></li>
    <li>✅ <b>Spring Security Websockets</b></li>
    <li>✅ <b>Secured Websockets Messaging with JWT</b></li>
    <li>✅ <b>Event-Driven Microservices</b></li>
    <li>✅ <b>Axon Event Sourcing</b></li>
    <li>✅ <b>Axon CQRS</b></li>
    <li>✅ <b>Axon Saga Transactions</b></li>
    <li>✅ <b>Axon Event Streaming</b></li>
    <li>✅ <b>Axon Event Store</b></li>
    <li>✅ <b>Axon Event Monitoring Console</b></li>
    <li>✅ <b>CQRS Query Projection with PostgreSQL Database</b></li>
    <li>✅ <b>CQRS Query Projection with MySQL Database</b></li>
    <li>✅ <b>CQRS Query Projection with MongoDB Database</b></li>
    <li>✅ <b>Keycloak Oauth2 Authorization Server</b></li>
    <li>✅ <b>Local Docker Environment</b></li>
    <li>✅ <b>Remote Debugging</b></li>
    <li>✅ <b>Zipkin Distributed Tracing</b></li>
  </ul>
</ul>

### 📖 Links

- [Axon Spring Boot Websocket Github Page by Ivan Franchin](https://github.com/ivangfr/axon-springboot-websocket)
- [Event-Driven Microservices, CQRS, SAGA, Axon, Spring Boot Udemy Course](https://www.udemy.com/course/spring-boot-microservices-cqrs-saga-axon-framework)

### 📖 Step By Step Guide

#### Local Docker Environment Setup:

```
sh docker-start.sh
```

- this script will build docker images and start environment with your code changes

- Warning! Make sure that Axon Server is initialized! (see `Axon Server Troubleshooting` below for more information)

```
sh docker-app-restart.sh customer
```

- this script will rebuild spring boot docker image for `customer` aplication and restart application with rebuilt image
- replace `customer` with the name of the application you want to rebuild and restart


#### Local Docker Environment Acceptance Test:

- open http://localhost:9000 in your Browser and switch between Swagger UI Pages

- open `Product` Swagger UI Page and create new product with some quantity

- open `Order` Swagger UI Page and create new order with some quantity: Make sure that order status is `Approved` and product quantity is requced by the order amount

- create new order with quantity that exceeds product quantity: Make sure that order status is `Rejected` and product quantity is rejected

- Use `Axon Server Console` to monitor Event Sourcing and Saga Transaction Events, related to `Approved` and `Rejected` Orders: http://localhost:8024/

- Warning! If Swagger UI fails to load on the first try, please, refresh the page!

- Warning! Sometimes switching between Swagger UI pages doesn't refresh Swagger UI completely and you might see wrong REST endpoints: just refresh the page and continue

- Warning! Sometimes REST endpoints return `504 Gateway Timeout`, just retry the REST API endpoint again

- For `POST` requests: click `Authorize` and use `admin/admin` or `user/user` for credentials (`clientId` should be `stock-app`)

- Try the following UI pages:
- `Customer UI`: http://localhost:9000/customer/
- `Restaurant UI`: http://localhost:9000/restaurant/
- `Food Ordering UI`: http://localhost:9000/food-ordering/
- use `admin/admin` or `user/user` for credentials
- Make sure that `Keycloak Authorization` and `WebSocket Connections` are working correctly for these pages

- See [Axon Spring Boot Websocket Github Page by Ivan Franchin](https://github.com/ivangfr/axon-springboot-websocket) for more details on `Customer UI`, `Restaurant UI` and `Food Ordering UI` pages

- Congratulations! You successfuly tested `Stock Tracking Swagger UI` and `Food Ordering UI`


### Remote Debugging

![Configuration to debug a containerized Java application from IntelliJ IDEA](documentation/06-14.png)

#### Axon Server Console

- Use `Axon Server Console` for `Event Monitoring and Tracing`: http://localhost:8024/


#### Axon Server Troubleshooting

- When you run `Axon Server` for the first time:
- Go to Axon Server Console: http://localhost:8024/
- Click `Finish` button in the setup configuration dialog

#### Zipkin Server

- Zipkin Server for Distributed Tracing is available here: http://localhost:9411/
