package br.edu.ifpb.photobean.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private Photographer follower;

    @ManyToOne
    @JoinColumn(name = "followee_id")
    private Photographer followee;
}
