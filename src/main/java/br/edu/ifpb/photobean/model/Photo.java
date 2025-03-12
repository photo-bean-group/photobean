package br.edu.ifpb.photobean.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
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

    private String imageUrl;

    @Lob
    @Column(name="imagemData", columnDefinition="blob")
    private byte[] imagemData;

    @ManyToOne  //uma foto está associada a um fotógrafo
    @JoinColumn(name = "photographer_id")// chave estrangeira na tabela
    private Photographer photographer;

    @OneToMany(mappedBy = "photo")// uma foto tem vários comentários
    private Set<Comment> comments;

    @OneToMany(mappedBy = "photo") // uma foto pode ter várias tags através de PhotoTag
    private Set<PhotoTag> photoTags;

    public Set<Tag> getTags() {
        Set<Tag> tags = new HashSet<>();
        for (PhotoTag photoTag : photoTags) {
            tags.add(photoTag.getTag());  // Assuming PhotoTag has a reference to Tag
        }
        return tags;
    }

    @ManyToMany // Relacionamento muitos-para-muitos para curtidas
    @JoinTable(
            name = "photo_likes",
            joinColumns = @JoinColumn(name = "photo_id"),
            inverseJoinColumns = @JoinColumn(name = "photographer_id")
    )
    private Set<Photographer> likes;

    public Photo(Photographer photographer) {
        this.photographer = photographer;
    }
}
