package com.digital_tok.template.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("SUBWAY") // dtype 으로 구분되는 값
@PrimaryKeyJoinColumn(name = "template_id") // template_id는 PK이자 FK임을 지정
@Table(name = "subway_template")
public class SubwayTemplate extends Template {

    @Column(name = "station_name", length = 10, nullable = false)
    private String stationName;

    @Column(name = "station_name_eng", length = 20, nullable = false)
    private String stationNameEng;

    @Column(name = "line_name", length = 10, nullable = false)
    private String lineName;
}
