package br.edu.ifpb.photobean.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(LikeId.class)
@Table(name = "photo_likes") // Evitando conflito com palavra reservada
public class Like {

    @Id
    @ManyToOne
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @Id
    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;
}

