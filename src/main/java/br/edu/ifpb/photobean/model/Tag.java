package br.edu.ifpb.photobean.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tagName;

    //   @OneToMany(mappedBy = "tag") // uma tag pode estar em varias fotos
    //  private Set<PhotoTag> photos;

    @OneToMany(mappedBy = "tag") // uma tag pode estar em várias fotos através de PhotoTag
    private Set<PhotoTag> photoTags;
}
