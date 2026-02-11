package com.digital_tok.template.domain;


import com.digital_tok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@SuperBuilder // 서브타입에서 슈퍼타입을 Builder로 사용가능하게해줌
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Table(name = "Template")
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    @Column(name = "template_image_url")
    private String templateImageUrl;

    @Column(name = "template_data_url")
    private String templateDataUrl;
}
