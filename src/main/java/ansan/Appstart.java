package ansan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // jpa가 엔티티의 변화 감시 기능 주입
@SpringBootApplication // 스프링부트 사용 주입
public class Appstart {
    public static void main(String[] args) {
        SpringApplication.run( Appstart.class , args );
    }
}
