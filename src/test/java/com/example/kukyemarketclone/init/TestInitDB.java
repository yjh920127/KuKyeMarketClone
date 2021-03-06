package com.example.kukyemarketclone.init;

import com.example.kukyemarketclone.entity.Message.Message;
import com.example.kukyemarketclone.entity.category.Category;
import com.example.kukyemarketclone.entity.member.Member;
import com.example.kukyemarketclone.entity.member.Role;
import com.example.kukyemarketclone.entity.member.RoleType;
import com.example.kukyemarketclone.exception.MemberNotFoundException;
import com.example.kukyemarketclone.exception.RoleNotFoundException;
import com.example.kukyemarketclone.repository.category.CategoryRepository;
import com.example.kukyemarketclone.repository.member.MemberRepository;
import com.example.kukyemarketclone.repository.message.MessageRepository;
import com.example.kukyemarketclone.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TestInitDB {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MessageRepository messageRepository;

    private String adminEmail = "admin@admin.com";
    private String member1Email = "member1@member.com";
    private String member2Email = "member2@member.com";
    private String password = "123456a!";

    @Transactional
    public void InitDB(){
        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
        initMessage();

    }

    private void initRole() {
        roleRepository.saveAll(
                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toList())
        );
    }

    private void initTestAdmin() {
        memberRepository.save(
          new Member(adminEmail,passwordEncoder.encode(password),"admin","admin",
                  List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                          roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new)))
        );
    }

    private void initCategory(){
        Category category1 = new Category("category1",null);
        Category category2 = new Category("category2",category1);
        categoryRepository.saveAll(List.of(category1,category2));
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member(member1Email,passwordEncoder.encode(password),"member1","member1",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))),
                        new Member(member2Email,passwordEncoder.encode(password),"member2","member2",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new)))
                )
        );
    }

    private void initMessage(){
        Member sender = memberRepository.findByEmail(getMember1Email()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(getMember2Email()).orElseThrow(MemberNotFoundException::new);
        IntStream.range(0,5).forEach(i -> messageRepository.save(new Message("content"+i, sender, receiver)));
    }

    public String getAdminEmail(){
        return adminEmail;
    }

    public String getMember1Email(){
        return member1Email;
    }

    public String getMember2Email(){
        return member2Email;
    }

    public String getPassword(){
        return password;
    }

}
