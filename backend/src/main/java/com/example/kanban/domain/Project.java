package com.example.kanban.domain;

import com.example.kanban.domain.enums.ProjectStatus;
import com.example.kanban.calculator.ProjectMetricsCalculator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project", schema = "kanban")
@EqualsAndHashCode(of = "id")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ProjectStatus status;

    private LocalDate scheduledStartDate;
    private LocalDate scheduledEndDate;

    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_responsible", schema = "kanban",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "responsible_id")
    )
    private Set<Responsible> responsibles = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Transient
    public Long getDaysOfDelay() {
        return ProjectMetricsCalculator.calculateDaysOfDelay(this, LocalDate.now());
    }

    @Transient
    public Double getRemainingTimePercentage() {
        return ProjectMetricsCalculator.calculateRemainingTimePercentage(this, LocalDate.now());
    }
}
