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

        // mappedBy indica que este é o lado "inverso" ou "não proprietário" do relacionamento.
    // Isso significa que a classe Photographer não possui a chave estrangeira no banco de dados.
    //O valor photographer refere-se ao nome do atributo na classe Photo que mapeia o relacionamento com Photographer
        @OneToMany(mappedBy = "photographer") // Um fotografo pode ter várias fotos associadas
        private Set<Photo> photos;

        @OneToMany(mappedBy = "follower")   //  Um fotógrafo pode seguir vários outros fotógrafos.
        private Set<Follow> following;

        @OneToMany(mappedBy = "followee")   //   Um fotógrafo pode ser seguido por vários outros fotógrafos.
        private Set<Follow> followers;
    }
