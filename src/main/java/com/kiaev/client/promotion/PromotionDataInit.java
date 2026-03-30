package com.kiaev.client.promotion;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component // 스프링이 구동될 때 자동으로 이 클래스를 찾아서 실행하게 해줍니다.
@RequiredArgsConstructor
public class PromotionDataInit implements CommandLineRunner {

    private final PromotionRepository promotionRepository;

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있다면 중복 생성하지 않도록 체크
        if (promotionRepository.count() == 0) {
            
            LocalDateTime now = LocalDateTime.now();

            // 1. [정상 노출] 활성화 + 현재 진행 중
            Promotion p1 = Promotion.builder()
                    .title("EV9 출시 기념 특별 할인!")
                    .content("EV9을 구매하시는 모든 분들께 특별한 혜택을 드립니다.")
                    .discountAmount(2000000)
                    .startDate(now.minusDays(5)) // 5일 전에 시작
                    .endDate(now.plusDays(10))   // 10일 후에 종료
                    .isActive(true)
                    .build();

            // 2. [정상 노출] 활성화 + 현재 진행 중 (할인 금액 없음)
            Promotion p2 = Promotion.builder()
                    .title("기아 전기차 무료 시승 이벤트")
                    .content("이번 주말, 가까운 대리점에서 무료 시승을 경험하세요.")
                    .startDate(now.minusDays(1))
                    .endDate(now.plusMonths(1)) // 1달 뒤 종료
                    .isActive(true)
                    .build();

            // 3. [미노출 테스트] 비활성화 (기간은 진행 중이나 관리자가 숨김 처리)
            Promotion p3 = Promotion.builder()
                    .title("숨겨진 임직원 프로모션")
                    .content("이 프로모션은 일반 사용자에게 보이면 안 됩니다.")
                    .discountAmount(5000000)
                    .startDate(now.minusDays(10))
                    .endDate(now.plusDays(10))
                    .isActive(false) // 비활성화!
                    .build();

            // 4. [미노출 테스트] 활성화되었으나 이미 종료된 프로모션
            Promotion p4 = Promotion.builder()
                    .title("지난달 EV6 깜짝 할인 (종료됨)")
                    .content("이미 기한이 지난 프로모션입니다.")
                    .discountAmount(1000000)
                    .startDate(now.minusMonths(2)) // 2달 전 시작
                    .endDate(now.minusMonths(1))   // 1달 전 종료
                    .isActive(true)
                    .build();

            // DB에 일괄 저장
            promotionRepository.save(p1);
            promotionRepository.save(p2);
            promotionRepository.save(p3);
            promotionRepository.save(p4);

            System.out.println("==========================================");
            System.out.println("✅ 테스트용 프로모션 더미 데이터 4건 생성 완료!");
            System.out.println("==========================================");
        }
    }
}