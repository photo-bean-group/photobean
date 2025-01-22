package br.edu.ifpb.photobean.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoFeedDTO {
    private Integer photoId;
    private String imageUrl;
    private String photographerName;
    private String photographerEmail;
}