package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("eom1");
        Member save = memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getId()).isEqualTo(save.getId());
        assertThat(findMember.getId()).isEqualTo(save.getId());
        assertThat(findMember).isEqualTo(member);
    }
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);



        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }
    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("aaa", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 19);
        Member findMember = findMembers.get(0);
        assertThat(findMember.getAge()).isEqualTo(20);

    }
    @Test
    public void findByUsername(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aaa = memberRepository.findByUsername(member1.getUsername());
        assertThat(aaa.get(0).getUsername()).isEqualTo(member1.getUsername());
    }

    @Test
    public void queryTest(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA",10);
        assertThat(member1).isEqualTo(result.get(0));
    }

    @Test
    public void findUsernameList(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> username = memberRepository.findUsername();
        for (String s : username) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto(){
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member member1 = new Member("AAA", 10);
        member1.setTeam(teamA);
        memberRepository.save(member1);

        List<MemberDto> usernameList = memberRepository.findMemberDto();

        for (MemberDto s : usernameList) {
            System.out.println("s = " + s);
        }

    }

    @Test
    public void findByNames(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }

    }
    @Test
    public void returnType(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

//        List
        List<Member> byNames = memberRepository.findListByUsername("AAA");
        for (Member byName : byNames) {
            System.out.println("byName.getUsername() = " + byName.getUsername());
        }
//        객체
        Member aaa = memberRepository.findMemberByUsername("AAA");

//        Optional
        Optional<Member> aaa1 = memberRepository.findOptionalByUsername("AAA");
    }



    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age  = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");
//when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

//when
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();
//        슬라이스 불가능
//        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        //페이지 계산 공식 적용...
        // totalPage = totalCount / size ...
        // 마지막 페이지 ...
        // 최초 페이지 ..
        //then


//        slice에 없는 기능
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 갯수
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지수



//        공통기능
        assertThat(content.size()).isEqualTo(3); // 불러온 갯수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 넘버
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


    }


    //벌크테스트
    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));
        //when
        int resultCount = memberRepository.bulkAgePlusB(20);

//        em.flush();
//        em.clear();

//        벌크연산은 DB에 바로 반영하기떄문에,
//         flush clear 해줘야 41로 찍힘
//        @Modifying(clearAutomatically = true) 있으면 생략가능
        List<Member> member5 = memberRepository.findByUsername("member5");
        Member member = member5.get(0);
        System.out.println("member = " + member); //

        //then
        assertThat(resultCount).isEqualTo(3);
    }


    //n+1 문제 발생하는 예시
    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush();
        em.clear();
//when
//        List<Member> members = memberRepository.findAll(); //select Member 1 번 Team 2번
//        List<Member> members = memberRepository.findMemberFetchJoin(); //select Member 1번에 team 까지 포함해서 조회
        List<Member> members = memberRepository.findMemberEntityGraph(); //select Member 1번에 team 까지 포함해서 조회
        //then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }


    @Test
    public void queryHint() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");
        em.flush(); //Update Query 실행X
    }


    //사용자 정의 Repository
    @Test
    public void callCustom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }


    @Test
    public void nativeQuery(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member result = memberRepository.findByNativeQuery("m1");
        System.out.println("result = " + result);
    }





}