package com.fitness.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter{
    
    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        RegisterRequest registerRequest = getUserDetails(token);

        if (userId == null) {
            userId = registerRequest.getKeycloakId();
        }

        if (userId != null && token != null) {

            String finalUserId = userId;

            return userService.validateUser(userId)
                              .flatMap(exist -> {           // exist is the return type of userService
                                if (!exist) {

                                    // register user
                                    if (registerRequest != null) {  // if we have registeration object then register them
                                        return userService.registerUser(registerRequest)
                                                          .then(Mono.empty());
                                    } 
                                    else {  // if we don't have registration object then do 
                                        return Mono.empty();
                                    }
                                } 
                                else {
                                    log.info("User already exist, skipping sync");
                                    return Mono.empty();
                                }
                              })
                              .then(Mono.defer(() -> {
                                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                                                               .header("X-User-ID", finalUserId)
                                                                               .build();
                                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                              }));
        }
        
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        // Implement logic to call Keycloak API and retrieve user details using the token
        // Map the response to a RegisterRequest object and return it

        try {
            String tokenWithoutBearer = token.replace("Bearer", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Now extracting everything from the claims set
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claims.getStringClaim("email"));
            registerRequest.setKeycloakId(claims.getStringClaim("sub"));
            registerRequest.setFirstName(claims.getStringClaim("given_name"));
            registerRequest.setLastName(claims.getStringClaim("family_name"));
            registerRequest.setPassword("dummy@123");

            return registerRequest;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}





// Flow of this code : 

/*


Correct Flow of This Filter

When a request comes:

Client
   ↓
Gateway
   ↓
KeycloakUserSyncFilter

The filter:

Reads JWT token from Authorization header.
Extracts:

email
sub (Keycloak ID)
first name
last name

Calls:
userService.validateUser(userId)
If user does not exist:
userService.registerUser(...)

Adds:
X-User-ID

to the request.

Forwards request to downstream services.
Gateway
   ↓
User Service
Activity Service
AI Service

This way every service knows which Keycloak user is making the request.

*/