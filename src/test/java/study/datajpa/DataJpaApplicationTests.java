package study.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //엔티티를 생성, 변경할 때 변경한 사람과 시간을 추적하고 싶으면 : 이를 DATA JPA로 하고 싶다면,
@SpringBootTest
class DataJpaApplicationTests {

	@Test
	void contextLoads() {
	}

}
