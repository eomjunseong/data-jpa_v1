package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername") //when Param?? --> :username //jpql 에 명확히 있을때
//    위에꺼 생략해도 작동하는데 .... -> JpaRepository<Membe,> 보고, Member.findByUsername 로 Named쿼리 먼저 찾고, 없으면 메소드 이름으로 쿼리~~~ 규칙 찾기 때문임
//    이기능 잘 안함 : Repository 에 바로 쿼리 작성하는 기능이 있어서 ,
//    그냥 이게 있구나~ 정도만 알고 넘어가기 
    List<Member> findByUsername(@Param("username") String username);
    
}
