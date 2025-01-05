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
    private Long id;

    @ManyToOne //Um registro de "seguir" está vinculado ao fotógrafo que está seguindo.
    @JoinColumn(name = "follower_id")
    private Photographer follower;

    @ManyToOne //Um registro de "seguir" está vinculado ao fotógrafo que está seguindo.
    @JoinColumn(name = "followee_id")
    private Photographer followee;//Um registro de "seguir" está vinculado ao fotógrafo que está sendo seguido.
}
