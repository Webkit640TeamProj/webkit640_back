package com.example.webkit640.service;


import com.example.webkit640.entity.Member;
import com.example.webkit640.repository.LocalMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {
    @Autowired
    private LocalMemberRepository localUserRepository;

    public Boolean findByEmail(String email) {
        return localUserRepository.existsMemberByEmail(email);
    }
    public Member findByEmailData(String email) {
        return localUserRepository.findByEmail(email);
    }

    public Member getByEmail(String email) {
        return localUserRepository.findByEmail(email);
    }

    public Member createMember(Member member) {
        return localUserRepository.save(member);
    }

    public Member getByCredentials(final String email, final String password) {
        return localUserRepository.findByEmailAndPassword(email,password);
    }

    public Member findByid(int id) {
        return localUserRepository.findById(id);
    }

    public void save(Member modifyMember) {
        localUserRepository.save(modifyMember);
    }
}
