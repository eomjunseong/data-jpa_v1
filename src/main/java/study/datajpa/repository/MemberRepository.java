package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> ,MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername") //when Param?? --> :username //jpql 에 명확히 있을때
//    위에꺼 생략해도 작동하는데 .... -> JpaRepository<Membe,> 보고, Member.findByUsername 로 Named쿼리 먼저 찾고, 없으면 메소드 이름으로 쿼리~~~ 규칙 찾기 때문임
//    이기능 잘 안함 : Repository 에 바로 쿼리 작성하는 기능이 있어서 ,
//    그냥 이게 있구나~ 정도만 알고 넘어가기
    List<Member> findByUsername(@Param("username") String username);

//    Reposoitory에 바로 쿼리 쓰는법 1 - entity
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username")String username, @Param("age")int age);

//    Reposoitory에 바로 쿼리 쓰는법 2 - 특정 속성
    @Query("select m.username from Member m")
    List<String> findUsername();

//    Reposoitory에 바로 쿼리 쓰는법 3 - DTO 
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

//    컬렉션 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

//    다양한 반환타입 지원함
    List<Member> findListByUsername(String name); //컬렉션 --없으면 empty 컬렉션 반환함, null X
    Member findMemberByUsername(String name); //단건 -- 없으면 null O
    Optional<Member> findOptionalByUsername(String name); //단건 Optional  -- 걍 Null일 수 도 있으니 Optional 쓰셈

//    페이징
//    Pageable : interface
//    PageRequest : 구현체

    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);


    @Query(value = "select m from Member m",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

// 벌크 수정
//    @Modifying //executeUpdate(); 효과 - 변경 효과
    @Modifying(clearAutomatically = true) //벌크 연산 나가고 em 자동으로 클리어 시킴
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlusB(@Param("age") int age);

    // 연관된 엔티티 한번에 조회
    @Query("select m from Member m  join fetch m.team")
    List<Member> findMemberFetchJoin();


    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프(쿼리도 짜는데, fetch join 추가 하고 싶을때)
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
//    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(String username);


    //이거 사용하면 변경감지 안됨 -> 조회용이라고 해서, 스냅샷을 안만듬)
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String member1);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);




}
