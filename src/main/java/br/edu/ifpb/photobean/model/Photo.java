package br.edu.ifpb.photobean.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter

public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  imageUrl;

    @Lob
    private byte[] imagemData;

    // @ManyToOne // um para muitos
    // @JoinColumn(name = "photographer_id", nullable = false)
    // private Photographer photographer; //  instanciar

    @ManyToOne  //uma foto esta associada a um fotografo
    @JoinColumn(name = "photographer_id")// chave estrangeira na tabela
    private Photographer photographer;


    @OneToMany(mappedBy = "photo")// uma foto tem varios comentarios
    private Set<Comment> comments;

    //  @OneToMany(mappedBy = "photo")// uma foto tem varias tags
    //  private Set<Tag> tags;
    @OneToMany(mappedBy = "photo") // uma foto pode ter várias tags através de PhotoTag
    private Set<PhotoTag> photoTags;


}
