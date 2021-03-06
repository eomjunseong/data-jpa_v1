package study.datajpa.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;









@Entity
@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@ToString(of={"id","username","age"})
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username")
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity{
//public class Member extends JpaBaseEntity{ --> 순수 jpa auditing

    @Id @GeneratedValue
    @Column(name="memebr_id")
    private Long id;
    private String username;
    private int age;

    //    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    public Member(String username) {
        this(username, 0);
    }
    public Member(String username, int age) {
        this(username, age, null);
    }
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    //연관 관계
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }


}
