package br.edu.ifpb.photobean.model;

import java.io.Serializable;
import java.util.Objects;

public class LikeId implements Serializable {
    private Integer photo;
    private Integer photographer;

    public LikeId() {
    }

    public LikeId(Integer photo, Integer photographer) {
        this.photo = photo;
        this.photographer = photographer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(photo, likeId.photo) && Objects.equals(photographer, likeId.photographer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photo, photographer);
    }
}
