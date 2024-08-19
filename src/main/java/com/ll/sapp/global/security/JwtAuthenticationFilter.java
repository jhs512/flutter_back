package com.ll.sapp.global.security;

import com.ll.sapp.member.Member;
import com.ll.sapp.member.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = authorization.split("Bearer ")[1];
        Map<String, Object> tokenData;

        Optional<Member> opMember = memberService.findByApiKey(apiKey);

        if (opMember.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Member member = opMember.get();

        List<String> authoritiesStringList = member.getAuthoritiesAsStringList();
        List<SimpleGrantedAuthority> authorities = authoritiesStringList.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        User user = new User(member.getUsername(), "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
