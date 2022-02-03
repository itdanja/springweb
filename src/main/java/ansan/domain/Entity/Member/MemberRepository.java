package ansan.domain.Entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity , Integer> {

    // 엔티티 검색  findby필드명
    Optional<MemberEntity> findBymid(String mid);
    // 기본키로 엔티티 검색[ 기본 ]
        // Optional<MemberEntity> findByid(String mid);
    // 해당 필드로 엔티티 검색 [ 생성 ]
        // Optional<MemberEntity> findBy필드명(자료형 필드명);
}
