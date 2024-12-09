# How to run the application

## Run using docker
There are 2 ways to run the application

### Via docker
```
docker pull anupamgogoi/ms-wallet-service:1.0.0
docker run --name ws -d -p 8080:8080 anupamgogoi/ms-wallet-service:1.0.0
```

### Via Gradle

Execute the below command
```
./gradlew bootRun

```

## Swagger endpoint
Access the below Swagger endpoint to access all the endpoints:
```
http://localhost:8080/swagger-ui.html
```

## Create wallet
```
curl -X 'POST' \
  'http://localhost:8080/api/v1/wallet/create' \
  -H 'accept: application/hal+json' \
  -H 'Content-Type: application/json' \
  -d '{
  "userName": "agogoi1",
  "email": "string111",
  "amount": 100
}'
```
userName and email must be unique. 
## Check balance
```
curl -X 'GET' \
  'http://localhost:8080/api/v1/wallet/{userName}/balance' \
  -H 'accept: application/hal+json'
```

## Deposit funds
```
curl -X 'POST' \
  'http://localhost:8080/api/v1/wallet/{userId}/deposit' \
  -H 'accept: application/hal+json' \
  -H 'Content-Type: application/json' \
  -d '{
  "userName": "agogoi",
  "amount": 1000
}'
```

## Withdraw funds
```
curl -X 'POST' \
  'http://localhost:8080/api/v1/wallet/{userId}/withdraw' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "userName": "agogoi",
  "amount": 90
}'
```

## Transfer funds
```
curl -X 'POST' \
'http://localhost:8080/api/v1/wallet/transfer' \
-H 'accept: application/hal+json' \
-H 'Content-Type: application/json' \
-d '{
"senderUserName": "agogoi",
"receiverUserName": "agogoi1",
"amount": 5
}'
```

## Historical funds
```
curl -X 'GET' \
  'http://localhost:8080/api/v1/wallet/{userName}/balance' \
  -H 'accept: application/hal+json'
```