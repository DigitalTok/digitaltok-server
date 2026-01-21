package com.digital_tok.domain;


import com.digital_tok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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
