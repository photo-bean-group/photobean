package br.edu.ifpb.photobean.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "photographer")
public class Photographer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Getter
    @Setter
    private boolean suspendedFromCommenting; // Suspensão apenas de comentários

    private String name;

    @Column(length = 255, unique = true, nullable = false)
    @Email
    @NotBlank
    private String email;

    private boolean suspended = false; // Novo campo para suspensão

    @OneToMany(mappedBy = "photographer")
    private Set<Photo> photos;

    @OneToMany(mappedBy = "follower")
    private Set<Follow> following;

    @OneToMany(mappedBy = "followee")
    private Set<Follow> followers;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
