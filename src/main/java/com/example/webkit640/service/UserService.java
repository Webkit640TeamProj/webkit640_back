package com.example.webkit640.service;


import com.example.webkit640.repository.LocalUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private LocalUserRepository localUserRepository;

    public Boolean findByEmail(String email) {
        if(localUserRepository.existsMemberByEmail(email)) {
            return true;
        } else {
            return false;
        }
    }
}
