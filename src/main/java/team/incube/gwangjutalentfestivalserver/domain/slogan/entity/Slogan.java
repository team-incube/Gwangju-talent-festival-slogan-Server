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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String slogan;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer classroom;

    @Column(nullable = false)
    private String phoneNumber;
}

