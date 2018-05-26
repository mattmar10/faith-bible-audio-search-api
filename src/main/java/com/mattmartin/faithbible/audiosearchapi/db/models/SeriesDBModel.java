package com.mattmartin.faithbible.audiosearchapi.db.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mattmartin.faithbible.audiosearchapi.db.config.StringListJsonUserType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "series", schema = "fbc_media")
@TypeDef(name = "StringListJsonUserType", typeClass = StringListJsonUserType.class)
public class SeriesDBModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="series_id")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String slug;

    @Column(name="image_url")
    private String imageURL;

    @Column private Integer likes;
    @Column private Integer shares;
    @Column private Integer plays;
    @Column private Boolean mapped;

    @Type(type="StringListJsonUserType")
    @Column private List<String> tags;

    @Column(name = "creation_date")
    private LocalDate createdDate;

    @JsonIgnore
    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "last_updated_date")
    private LocalDate lastUpdatedDate;

    @JoinColumn(name="series_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SermonDBModel> sermons = new ArrayList<>();

    public SeriesDBModel(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public Integer getPlays() {
        return plays;
    }

    public void setPlays(Integer plays) {
        this.plays = plays;
    }

    public Boolean getMapped() {
        return mapped;
    }

    public void setMapped(Boolean mapped) {
        this.mapped = mapped;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public List<SermonDBModel> getSermons() {
        return sermons;
    }

    public void setSermons(List<SermonDBModel> sermons) {
        sermons.forEach(s -> s.setSeries(this));
        this.sermons = sermons;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeriesDBModel that = (SeriesDBModel) o;

        if (!id.equals(that.id)) return false;
        if (!title.equals(that.title)) return false;
        if (!slug.equals(that.slug)) return false;
        if (imageURL != null ? !imageURL.equals(that.imageURL) : that.imageURL != null) return false;
        if (!mapped.equals(that.mapped)) return false;
        return sermons.equals(that.sermons);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + slug.hashCode();
        result = 31 * result + (imageURL != null ? imageURL.hashCode() : 0);
        result = 31 * result + mapped.hashCode();
        result = 31 * result + sermons.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SeriesDBModel{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", slug='").append(slug).append('\'');
        sb.append(", imageURL='").append(imageURL).append('\'');
        sb.append(", likes=").append(likes);
        sb.append(", shares=").append(shares);
        sb.append(", plays=").append(plays);
        sb.append(", mapped=").append(mapped);
        sb.append(", tags=").append(tags);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", lastUpdatedBy='").append(lastUpdatedBy).append('\'');
        sb.append(", lastUpdatedDate=").append(lastUpdatedDate);
        sb.append(", sermons=").append(sermons);
        sb.append('}');
        return sb.toString();
    }
}
