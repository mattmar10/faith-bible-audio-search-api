package com.mattmartin.faithbible.audiosearchapi.db.models;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "sermons", schema = "fbc_media")
public class SermonDBModel {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String title;
    private String href;
    private String speaker;
    private LocalDate date;
    private String series;
    private Integer seriesId;
    private String image;
    private Integer likes;
    private Integer shares;
    private Integer plays;
    private Boolean mapped;

    public SermonDBModel(){}
}
