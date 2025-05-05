package team.incube.gwangjutalentfestivalserver.domain.slogan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "slogans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Slogan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String slogan;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private Integer grade;

    @Column(name = "class_num", nullable = false)
    private Integer classNum;

    @Column(nullable = false)
    private String phone;
}

