package com.kiaev.client.board;

import com.kiaev.client.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 텅 비어있어도 괜찮습니다! 마법이 일어날 거예요.
}