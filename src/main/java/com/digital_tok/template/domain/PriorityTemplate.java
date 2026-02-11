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
@Table(name = "priority_template")
public class PriorityTemplate extends Template {

    @Column(name = "priority_type", length = 50)
    private String priorityType;
}
