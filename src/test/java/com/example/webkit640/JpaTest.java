package com.example.webkit640;

import com.example.webkit640.entity.Member;
import com.example.webkit640.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class JpaTest {
    MemberService memberService = new MemberService();

    @Test
    void showAll() {
        List<Member> list = memberService.getAllMembers();

        System.out.println(list.toString());
    }
}
