package com.example.kukyemarketclone.entity.member;

import com.example.kukyemarketclone.entity.common.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Entity
@NamedEntityGraph(  //연관된 엔티티들을 함께 조회 가능
        //Member.rolse 그래프에서 Member의 rolrs를 조회
        name="Member.roles",//엔티티 그래프 이름 설정
        attributeNodes = @NamedAttributeNode(value = "roles", subgraph = "Member.roles.role"),//함께 조회해야하는 엔티티의 필드명

        //서브그래프 이용시 연관 엔티티의 연관 엔티티까지 함께 조회
        //Member.roles.role 서브그래프에서 MemberRole의 role 조회
        subgraphs = @NamedSubgraph(name = "Member.roles.role", attributeNodes = @NamedAttributeNode("role"))

)
public class Member extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable=false, length = 30, unique=true) //unique로 인해 인덱스 생성 + 제약조건 추가
    private String email;

    private String password;

    @Column(nullable=false, length = 20)
    private String username;

    @Column(nullable=false, length=20, unique = true) //unique로 인해 인덱스 생성 + 제약조건 추가
    private String nickname;

    /*
    * @ManyTomany로 설정할 수 도 있지만 사용자 권한에 어떤 추가적인 데이터가 기록될지 모르기 때문에 유연성을 위해 @oneToMany
    * Member가 저장되거나 제거될때 MemberRole도 연쇄적으로 저장 & 제거가 가능하도록 cscade = All , orphanRemoval = true
    * 권한 등급은 많지 않지만 조회시의 검색 성능 향상을 위해 Set으로 선언
    * */
    @OneToMany(mappedBy= "member", cascade=CascadeType.ALL, orphanRemoval = true )
    private Set<MemberRole> roles;

    public Member(String email,String password, String username, String nickname, List<Role> roles){
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles.stream().map(r -> new MemberRole(this, r)).collect(toSet());
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }



}
