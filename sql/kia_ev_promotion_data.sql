-- 1. 기존 테이블 삭제
DROP TABLE IF EXISTS `PROMOTION_TBL`;

-- 2. 새 프로모션 테이블 생성 (discount_amount 복구!)
CREATE TABLE `PROMOTION_TBL` (
    `promotion_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(20) NOT NULL COMMENT '프로모션 유형(DISCOUNT, GIFT, EVENT)',
    `title` VARCHAR(200) NOT NULL COMMENT '프로모션 제목',
    `sub_title` VARCHAR(255) COMMENT '소제목 (짧은 설명)',
    `content` TEXT COMMENT '상세 내용',
    `target_model` VARCHAR(50) DEFAULT 'ALL' COMMENT '대상 차량 모델',
    `discount_amount` INT DEFAULT 0 COMMENT '할인 금액', -- 👈 명세서대로 복구!
    `start_date` DATETIME NOT NULL COMMENT '시작일',
    `end_date` DATETIME NOT NULL COMMENT '종료일',
    `banner_order` INT DEFAULT 0 COMMENT '배너 노출 순서',
    `banner_image_url` VARCHAR(255) COMMENT '배너 이미지 경로',
    `is_active` TINYINT(1) DEFAULT 1 COMMENT '활성화 여부(1:Y, 0:N)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. 테스트용 10개 예시 데이터 삽입 (할인율 제거, 금액으로 통일)
INSERT INTO `PROMOTION_TBL` 
(`type`, `title`, `sub_title`, `content`, `target_model`, `discount_amount`, `start_date`, `end_date`, `banner_order`, `banner_image_url`, `is_active`) 
VALUES 
('DISCOUNT', 'EV6 GT 라인 특별 지원', '압도적인 퍼포먼스, EV6를 더 가깝게', 'EV6 GT 모델 한정 특별 혜택 제공', 'EV6', 1500000, '2026-03-01', '2026-06-30', 1, '/images/promo/ev6_gt.jpg', 1),
('GIFT', 'Ray EV 도심 주행 패키지', '취등록세 지원부터 충전 크레딧까지!', '도심 주행에 최적화된 레이 EV 구매 시 혜택', 'Ray EV', 0, '2026-03-15', '2026-12-31', 2, '/images/promo/ray_gift.jpg', 1),
('DISCOUNT', 'EV9 패밀리 감사제', '최고의 플래그십 SUV, 특별 할인', '다자녀 가구를 위한 EV9 특별 할인', 'EV9', 3000000, '2026-03-20', '2026-05-31', 3, '/images/promo/ev9_family.jpg', 1),
('EVENT', 'COMING SOON: EV3', '기아의 새로운 콤팩트 SUV, 가장 먼저 만나보세요', 'EV3 사전 예약 알림 신청', 'EV3', 0, '2026-05-01', '2026-05-31', 4, '/images/promo/ev3_launch.jpg', 0),
('DISCOUNT', '기아 EV 봄맞이 페스타', '전기차 전 모델 대상 스마트 할부 혜택', '봄 시즌 한정 스마트 할부 프로그램', 'ALL', 2000000, '2026-03-01', '2026-04-30', 5, '/images/promo/spring_festa.jpg', 1),
('GIFT', '2026 해맞이 EV 캠핑 세트', '전기차 계약 고객 대상 캠핑 의자 증정', '새해 맞이 V2L 활용 캠핑 용품 증정', 'ALL', 0, '2026-01-01', '2026-02-28', 6, '/images/promo/newyear.jpg', 0),
('DISCOUNT', '노후차 교체 특별 지원금', '10년 이상 노후차 폐차 후 EV 구매 시 추가 할인', '환경 보호를 위한 노후차 교체 캠페인', 'ALL', 300000, '2026-03-10', '2026-09-30', 7, '/images/promo/oldcar_change.jpg', 1),
('GIFT', '기아 커넥트 스토어 이용권', '원격 주차, 스트리밍 서비스 1년 무료 체험', '기아 커넥트 스토어의 모든 기능을 1년간 무료로', 'ALL', 0, '2026-03-01', '2027-03-01', 8, '/images/promo/connect_free.jpg', 1),
('DISCOUNT', '니로 EV 재고 특별전', '한정 수량! 바로 출고 가능한 니로 EV 특별 할인', '빠른 출고와 특별 할인을 동시에', 'Niro EV', 2000000, '2026-03-25', '2026-04-25', 9, '/images/promo/niro_sale.jpg', 1),
('EVENT', '내부 테스트 프로모션', '시스템 테스트용 데이터입니다. (노출금지)', '관리자 테스트 목적의 데이터', 'ALL', 0, '2026-03-01', '2099-12-31', 10, '/images/promo/test.jpg', 0);

-- 4. 최종 확인
SELECT * FROM `PROMOTION_TBL` ORDER BY `banner_order` ASC;