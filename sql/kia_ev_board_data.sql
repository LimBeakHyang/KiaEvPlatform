-- 1. 기존 테이블이 있다면 삭제
DROP TABLE IF EXISTS `BOARD_TBL`;

-- 2. 통합 게시판 테이블 생성 (MySQL 버전에 맞게 데이터 타입 최적화)
CREATE TABLE `BOARD_TBL` (
    `board_no` BIGINT NOT NULL AUTO_INCREMENT COMMENT '게시글번호 (PK)',
    `board_type` VARCHAR(20) COMMENT '게시글타입 (NOTICE: 공지사항, INQUIRY: 문의글)',
    `member_no` BIGINT COMMENT '회원번호 (문의글 작성자)',
    `admin_no` BIGINT COMMENT '관리자번호 (공지 작성 또는 답변자)',
    `title` VARCHAR(2000) NOT NULL COMMENT '제목',
    `content` TEXT NOT NULL COMMENT '내용', -- (VARCHAR(2000) 대신 더 넉넉한 TEXT 타입 추천)
    `is_pinned` CHAR(1) DEFAULT 'N' COMMENT '상단고정여부 (Y/N)',
    `inquiry_status` VARCHAR(20) COMMENT '문의상태 (WAITING: 답변대기, COMPLETED: 답변완료)',
    `answer_content` TEXT COMMENT '답변내용',
    `answer_date` DATETIME COMMENT '답변일시',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '조회수',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    `deleted_yn` CHAR(1) NOT NULL DEFAULT 'N' COMMENT '삭제여부 (Y/N)',
    PRIMARY KEY (`board_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. 테스트용 예시 데이터 삽입 (다양한 케이스)
INSERT INTO `BOARD_TBL` 
(`board_type`, `member_no`, `admin_no`, `title`, `content`, `is_pinned`, `inquiry_status`, `answer_content`, `answer_date`, `view_count`, `deleted_yn`) 
VALUES 
-- [1. 공지사항] 상단 고정 (필독 공지)
('NOTICE', NULL, 1, '[필독] 기아 EV 플랫폼 서비스 점검 안내', '더 나은 서비스를 위해 4월 1일 새벽 2시부터 4시까지 서버 점검이 진행됩니다.', 'Y', NULL, NULL, NULL, 150, 'N'),

-- [2. 공지사항] 일반 공지
('NOTICE', NULL, 1, '2026년 신규 전기차 보조금 개편 안내', '올해 새롭게 개편된 국고 보조금 및 지자체 보조금 지급 기준을 안내해 드립니다.', 'N', NULL, NULL, NULL, 45, 'N'),

-- [3. 문의글] 답변 대기 중인 일반 문의
('INQUIRY', 101, NULL, 'EV6 출고 대기기간 문의', '어제 계약했는데 대략적인 출고 일정을 알 수 있을까요?', 'N', 'WAITING', NULL, NULL, 5, 'N'),

-- [4. 문의글] 답변이 완료된 문의 (관리자 답변 포함)
('INQUIRY', 102, 2, '가정용 충전기 설치 조건', '단독주택인데 설치비 지원이 되나요?', 'N', 'COMPLETED', '안녕하세요 고객님, 단독주택의 경우 이번 프로모션을 통해 설치비가 전액 지원됩니다. 상세 내용은...', '2026-03-30 14:00:00', 12, 'N'),

-- [5. 문의글] 사용자가 삭제한 게시글 (deleted_yn = 'Y')
('INQUIRY', 103, NULL, '렌터카 문의드립니다.', '아, 렌터카 메뉴를 방금 찾았네요. 글 지웁니다.', 'N', 'WAITING', NULL, NULL, 1, 'Y');

-- 4. 최종 확인
SELECT * FROM `BOARD_TBL` ORDER BY `is_pinned` DESC, `board_no` DESC;