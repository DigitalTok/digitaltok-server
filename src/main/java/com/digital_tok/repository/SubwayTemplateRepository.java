package com.digital_tok.repository;

import com.digital_tok.domain.SubwayTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayTemplateRepository extends JpaRepository<SubwayTemplate, Long> {

    // 오름차순으로 정렬된 지하철역 정보를 반환
    // 추후에 페이징 기능 추가 필요할수도
    List<SubwayTemplate> findAllByOrderByStationNameAsc();

}
