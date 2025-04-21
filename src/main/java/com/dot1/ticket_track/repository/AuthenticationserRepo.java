package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.dto.JwtAuthenticationResponcse;
import com.dot1.ticket_track.dto.RefreshTokenRequest;
import com.dot1.ticket_track.dto.SigninRequest;
import com.dot1.ticket_track.dto.SignupRequest;
import com.dot1.ticket_track.entity.mUserLogin_demo;

public interface AuthenticationserRepo {

    mUserLogin_demo signup(SignupRequest signupRequest);

    JwtAuthenticationResponcse signin(SigninRequest signinRequest);

    JwtAuthenticationResponcse refreshToken (RefreshTokenRequest refreshTokenRequest);
}
