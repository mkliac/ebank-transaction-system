package com.example.ebanktransactionsystem.service;

import com.example.ebanktransactionsystem.controller.AuthenticationRequest;
import com.example.ebanktransactionsystem.controller.AuthenticationResponse;
import com.example.ebanktransactionsystem.model.User;
import com.example.ebanktransactionsystem.repository.UserRepository;
import com.example.ebanktransactionsystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        log.info("authenticate user:" + request.getUserId());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserId(),
                        request.getPassword()
                )
        );
        User user = userRepository.getUserByUserId(request.getUserId())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);

        log.info("return token:" + jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void addUser(User user){

        log.info("user:" + user.getUserId() +
                " save successfully");

        userRepository.save(user);
    }

    public void resetUserDb(){

        log.info("remove all users from repository");

        userRepository.deleteAll();
    }
}
