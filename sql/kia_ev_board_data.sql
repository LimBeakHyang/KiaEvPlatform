-- 1. 안전 업데이트 모드 잠시 해제
SET SQL_SAFE_UPDATES = 0;

-- 2. 분류(board_type) 영어를 한글로 일괄 변경
UPDATE BOARD_TBL SET board_type = '공지사항' WHERE board_type = 'NOTICE';
UPDATE BOARD_TBL SET board_type = '일반문의' WHERE board_type = 'INQUIRY';
UPDATE BOARD_TBL SET board_type = '전기차문의' WHERE board_type = 'EV_INQUIRY';
UPDATE BOARD_TBL SET board_type = '고객센터' WHERE board_type = 'CS_CENTER';

-- 3. 개별 게시글 상세 내용 수정 (PK인 board_no를 사용하므로 안전함)
UPDATE BOARD_TBL SET title = '[필독] 기아 EV 멤버십 서비스 개편 및 혜택 안내', is_pinned = 'Y', priority = 100 WHERE board_no = 1;
UPDATE BOARD_TBL SET title = 'EV6 롱레인지 모델 완충 시 주행 가능 거리가 궁금합니다.', is_pinned = 'N', priority = 0 WHERE board_no = 2;
UPDATE BOARD_TBL SET title = '기아 커넥트 앱 설치 및 연동 방법 알려주세요.', is_pinned = 'N', priority = 0 WHERE board_no = 3;
UPDATE BOARD_TBL SET title = '26년 상반기 기아 전기차 소프트웨어 무상 업데이트 캠페인', is_pinned = 'Y', priority = 50 WHERE board_no = 4;

-- 4. 삭제 여부 초기화
UPDATE BOARD_TBL SET deleted_yn = 'N';

-- 5. 변경사항 저장
COMMIT;

-- 6. 다시 안전 모드 켜기 (실수 방지를 위해 다시 켜두는 게 좋습니다)
SET SQL_SAFE_UPDATES = 1;




-- 1. 안전 모드 해제 (혹시 모를 에러 방지)
SET SQL_SAFE_UPDATES = 0;

-- 2. 대량 데이터 삽입 (다양한 분류와 제목)
INSERT INTO BOARD_TBL (board_type, title, content, notice_yn, priority, deleted_yn) VALUES 
('공지사항', '[이벤트] 기아 EV 멤버십 가입하고 GS칼텍스 쿠폰 받으세요', '이벤트 내용입니다.', 'Y', 90, 'N'),
('공지사항', '2026년형 EV6 출시 기념 시승회 안내', '시승회 안내 내용입니다.', 'Y', 80, 'N'),
('일반문의', 'EV9 출고 대기 기간이 보통 어느 정도 되나요?', '출고 대기 관련 문의입니다.', 'N', 0, 'N'),
('전기차문의', '겨울철 히터 사용 시 주행거리 감소 폭이 궁금합니다.', '배터리 효율 문의입니다.', 'N', 0, 'N'),
('고객센터', '기아 커넥트 앱 로그인이 안 됩니다. 해결 방법 있나요?', '앱 오류 문의입니다.', 'N', 0, 'N'),
('일반문의', '기아 EV6 외장 컬러 중에서 어떤 게 제일 인기 많나요?', '컬러 추천 문의입니다.', 'N', 0, 'N'),
('전기차문의', '완속 충전기와 급속 충전기 케이블 차이점', '충전 방식 문의입니다.', 'N', 0, 'N'),
('일반문의', '전기차 보조금 거주지 이전 시 반납해야 하나요?', '보조금 법규 문의입니다.', 'N', 0, 'N'),
('고객센터', '정비 예약하고 싶은데 가장 가까운 오토큐 찾는 법', '정비소 문의입니다.', 'N', 0, 'N'),
('전기차문의', 'V2L로 캠핑용 인덕션 사용 가능한가요?', 'V2L 활용 문의입니다.', 'N', 0, 'N'),
('일반문의', '기아 멤버스 포인트로 충전비 결제하는 방법', '포인트 사용 문의입니다.', 'N', 0, 'N'),
('전기차문의', '회생제동 단계 조절하는 패들 쉬프트 사용법', '운전 조작 문의입니다.', 'N', 0, 'N'),
('고객센터', '차량 매뉴얼 책자를 분실했는데 새로 받을 수 있나요?', '매뉴얼 문의입니다.', 'N', 0, 'N'),
('일반문의', '니로 EV랑 EV6 중에서 고민 중입니다. 추천 부탁드려요.', '차량 비교 문의입니다.', 'N', 0, 'N'),
('전기차문의', '충전소 에티켓! 완충 후 이동 안 하는 차 신고 방법', '매너 관련 문의입니다.', 'N', 0, 'N'),
('일반문의', '기아 전기차 중고 시세가 궁금합니다.', '중고차 문의입니다.', 'N', 0, 'N'),
('고객센터', '신차 패키지 선팅 추천 부탁드립니다.', '튜닝 문의입니다.', 'N', 0, 'N'),
('전기차문의', '원격 스마트 주차 보조 기능 좁은 주차장에서 잘 되나요?', '기능 테스트 문의입니다.', 'N', 0, 'N'),
('일반문의', '테슬라 슈퍼차저를 기아차도 이용할 수 있게 되나요?', '인프라 문의입니다.', 'N', 0, 'N'),
('고객센터', '차량 내부에서 소음이 나는데 점검 대상인가요?', 'AS 문의입니다.', 'N', 0, 'N');

-- 3. 변경사항 저장
COMMIT;

SELECT * 
FROM board_tbl;