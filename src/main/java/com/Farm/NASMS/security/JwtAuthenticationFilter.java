package com.Farm.NASMS.security;

import java.io.IOException;
import java.security.Security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Farm.NASMS.Service.CustomuserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomuserDetailsService customUserDetailsService;
    public JwtAuthenticationFilter(JwtUtil jwtUtil,CustomuserDetailsService customuserDetailsService){
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customuserDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException{
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String emailAddress = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
            try{
                emailAddress = jwtUtil.extractUserName(token);
            } catch(Exception ignored){

            }
        }

        if(emailAddress != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try{
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(emailAddress);
                if (jwtUtil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken); 
                }
            }catch (Exception ignored){

            }
        }
        filterChain.doFilter(request, response);
    }

}
