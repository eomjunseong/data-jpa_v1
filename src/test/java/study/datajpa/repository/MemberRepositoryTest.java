package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository MemberRepository;

    @Test
    public void testMember(){
        Member member = new Member("eom1");
        Member save = MemberRepository.save(member);
        Member findMember = MemberRepository.findById(member.getId()).get();

        assertThat(findMember.getId()).isEqualTo(save.getId());
        assertThat(findMember.getId()).isEqualTo(save.getId());
        assertThat(findMember).isEqualTo(member);
    }
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        MemberRepository.save(member1);
        MemberRepository.save(member2);



        //단건 조회 검증
        Member findMember1 = MemberRepository.findById(member1.getId()).get();
        Member findMember2 = MemberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = MemberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = MemberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        MemberRepository.delete(member1);
        MemberRepository.delete(member2);

        long deletedCount = MemberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }
    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aaa", 20);

        MemberRepository.save(member1);
        MemberRepository.save(member2);

        List<Member> findMembers = MemberRepository.findByUsernameAndAgeGreaterThan("aaa", 19);
        Member findMember = findMembers.get(0);
        assertThat(findMember.getAge()).isEqualTo(20);

    }
    @Test
    public void findByUsername(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        MemberRepository.save(member1);
        MemberRepository.save(member2);

        List<Member> aaa = MemberRepository.findByUsername(member1.getUsername());
        assertThat(aaa.get(0).getUsername()).isEqualTo(member1.getUsername());
    }
}