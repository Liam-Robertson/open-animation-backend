package com.openAnimation.app.models;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commentary")
public class Commentary {
    @Id
    @SequenceGenerator(
            name = "commentary_sequence",
            sequenceName = "commentary_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "commentary_sequence"
    )
    private Long id;

    @Column(name="comment")
    private String comment;
}
