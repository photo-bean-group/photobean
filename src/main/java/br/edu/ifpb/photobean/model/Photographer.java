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

        private String name;

        @Column(length = 255, unique = true, nullable = false)
        @Email
        @NotBlank
        private String email;

        @OneToMany(mappedBy = "photographer")
        private Set<Photo> photos;

        @OneToMany(mappedBy = "follower")
        private Set<Follow> following;

        @OneToMany(mappedBy = "followee")
        private Set<Follow> followers;
    }
