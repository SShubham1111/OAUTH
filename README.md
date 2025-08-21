OAuth2 Authorization Server & Resource Server (Java + Netty)

This project is a lightweight OAuth2 demo built with plain Java and Netty (no Spring).  
It demonstrates the OAuth2 workflow with endpoints for client registration, authorization, token issuance, and resource access.


Tech Stack :

1. Java 8
2. Netty
3. Gson
4. Org.json
5. Eclipse IDE


Project Structure:

OAuth2.0
├── src/
│   ├── com/http/server/
│   │   └── NettyHttpServer.java       # Core HTTP server (runs on port 8080)
│   ├── com/oauth2/startup/
│   │   └── OAuth2Bootstrap.java       # Main entry point
│   ├── com/oauth2/controller/         # Request handlers (Register, Auth, Token, Resource)
│   ├── com/oauth2/model/              # Data models
│   └── com/oauth2/store/              # In-memory storage for clients & tokens
├── lib/
│   ├── netty-all-4.1.9.Final.jar
│   ├── gson-2.2.2.jar
│   └── org.json.jar
└── README.md


Features :

1. Register Client – `/register`
2. Authorize User – `/auth`
3. Issue Access Token – `/token`
4. Access Protected Resource – `/resource`  


Implements a simplified OAuth2 flow :

1. User registers → receives `client_id` and `client_secret`
2. Authorize with username/password → validate client
3. Exchange client credentials for an access token
4. Use the access token to call a protected resource


Getting Started :

1) Prerequisites
- JDK 1.8
- Eclipse IDE for Java Developers
- Postman

2) Import in Eclipse
1. Unzip the project (`OAuth2.zip` → `OAuth2.0/`).
2. Open Eclipse → `File → Import → Existing Projects into Workspace`.
3. Select the `OAuth2.0` folder.
4. Fix Build Path if needed:
   - Add the `lib/` JARs (`netty-all`, `gson`, `org.json`) to Project → Build Path → Libraries.
   - Set Java Compiler Level to `1.8`.

3) Run the Server
- Main class: `com.oauth2.startup.OAuth2Bootstrap`
- Run as → Java Application
- Default port: 8080

To change port: edit `NettyHttpServer.java` → `PORT = 8080;`


API Endpoints :

1) Register Client
POST `/register`
 
Headers:
Content-Type: application/json
applicationType: AuthorizationServer

Body:
json
{
  "name": "demoUser",
  "password": "demoPass123",
  "address": "Demo Address",
  "contact": "9999999999",
  "emailid": "demo@example.com"
}

Response:
json
{ "client_id": "...", "client_secret": "..." }

2) Authorize
POST `/auth`

Headers:
name: demoUser
password: demoPass123
applicationType: AuthorizationServer

Response:
json
{ "client_id": "...", "client_secret": "..." }

3) Token
POST `/token`

Headers:
clientid: <client_id>
clientsecret: <client_secret>
applicationType: AuthorizationServer

Response:
json
{ "access_token": "...", "token_type": "Bearer" }

4) Protected Resource
POST `/resource`

Headers:
Authorization: Bearer <access_token>
applicationType: ResourceServer

Response:
json
{ "message": "Resource Accessed" }

Testing with Postman :
You can test step by step (Register → Auth → Token → Resource) or import the ready-made collection:
Netty_Demo.postman_collection.json


Steps:

1. Open Postman → Import → Select JSON file.
2. Update `{{baseUrl}}` if port ≠ `8080`.
3. Run requests in order.


Troubleshooting :

1. 404 Not Found → Wrong path (must be `/register`, `/auth`, `/token`, `/resource`) or wrong HTTP method (must be POST).
2. 400 Bad Request → Missing headers/body fields.
3. 403 Forbidden → Wrong client credentials or invalid token.
4. Port in use → Change `PORT` in `NettyHttpServer.java`.


Notes :

1. This project is for educational/demo purposes only.
2. No persistence layer (all data stored in memory).
3. Not production-ready (no refresh tokens, limited error handling).


