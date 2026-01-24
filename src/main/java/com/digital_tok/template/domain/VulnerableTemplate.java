package com.digital_tok.template.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("VULNERABLE")
@PrimaryKeyJoinColumn(name = "template_id")
@Table(name = "vulnerable_template")
public class VulnerableTemplate extends Template {

    @Column(name = "vulnerable_type", length = 50)
    private String vulnerableType;
}
