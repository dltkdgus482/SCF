package com.scf.user.member.application.service;

import com.scf.user.member.domain.entity.Member;
import com.scf.user.member.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return member;
    }
}
