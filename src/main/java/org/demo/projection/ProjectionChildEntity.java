package org.demo.projection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class ProjectionChildEntity {

    @Id
    @GeneratedValue
    private Long id;

    private int counter;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProjectionEntity parent;
}
