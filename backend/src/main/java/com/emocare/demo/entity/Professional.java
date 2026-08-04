package com.emocare.demo.entity;

import com.emocare.demo.entity.enums.ProfessionalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "professionals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professional {

    @Id
    @Column(name = "user_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "crp")
    private String crp;

    @Column(name = "biography")
    private String biography;

    @Column(name = "therapeutic_approach")
    private String therapeuticApproach;

    @Column(name = "experience_years")
    private Short experienceYears;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private ProfessionalStatus status = ProfessionalStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt; // Mapeia o TIMESTAMPTZ do Postgres

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt; // Mapeia o TIMESTAMPTZ do Postgres

    @ManyToMany
    @JoinTable(
            name = "professional_specialties",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private Set<Specialty> specialties = new HashSet<>();

    @Column(name = "buffer_minutes", nullable = false, columnDefinition = "smallint default 0")
    private Short bufferMinutes = 10;

    @PrePersist
    public void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
