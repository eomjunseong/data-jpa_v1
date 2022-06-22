package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;


//사용자 정의 Repositroy
//이 Intetface를 상속하는 애랑 이름을 맞춰야함
//ex) MemberRepository --> MemberRepositoryImpl
//Impl이 붙어야함
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
