package br.edu.ifpb.photobean.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public Photo(Photographer photographer) {
        this.photographer = photographer;
    }
}
