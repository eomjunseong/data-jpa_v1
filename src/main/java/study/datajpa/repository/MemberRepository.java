package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

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

}
