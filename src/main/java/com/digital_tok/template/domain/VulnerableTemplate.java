package com.digital_tok.template.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("VULNERABLE")
@PrimaryKeyJoinColumn(name = "template_id")
@Table(name = "vulnerable_template")
public class VulnerableTemplate extends Template {

    @Column(name = "vulnerable_type", length = 50)
    private String vulnerableType;
}
