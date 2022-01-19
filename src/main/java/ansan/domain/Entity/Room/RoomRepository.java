package ansan.domain.Entity.Room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository< RoomEntity , Integer > {
    // 엔티티 조작 인테페이스
}

