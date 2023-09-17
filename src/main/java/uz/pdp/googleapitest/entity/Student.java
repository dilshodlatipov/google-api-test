package uz.pdp.googleapitest.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.googleapitest.enums.Sex;
import uz.pdp.googleapitest.enums.ClassLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(nullable = false, name = "class_level")
    @Enumerated(EnumType.STRING)
    private ClassLevel classLevel;
    @Column(nullable = false, name = "home_state")
    private String homeState;
    @Column(nullable = false)
    private String major;
    @Column(nullable = false)
    private String activity;
}
