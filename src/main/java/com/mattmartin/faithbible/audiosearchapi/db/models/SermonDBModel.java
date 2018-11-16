package com.mattmartin.faithbible.audiosearchapi.db.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.mattmartin.faithbible.audiosearchapi.db.config.StringListJsonUserType;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sermons", schema = "fbc_media")
@TypeDef(name = "StringListJsonUserType", typeClass = StringListJsonUserType.class)
public class SermonDBModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sermon_id")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(name = "mp3_url")
    private String mp3Url;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column private String speaker;

    @Column private LocalDate date;

    @Column(name="image_url")
    private String imageUrl;

    @Column(nullable = false)
    private String slug;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DBModelUtils.DATE_FORMAT )
    private LocalDate lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name="series_id", nullable = false)
    private SeriesDBModel series;

    public SermonDBModel(){}

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public SeriesDBModel getSeries() {
        return series;
    }

    public void setSeries(SeriesDBModel series) {
        this.series = series;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public SermonDBModel updateFromOtherSermonDBModel(final SermonDBModel other){

        if(other.getTitle() != null){
            this.title = other.getTitle();
        }

        if (other.getMp3Url() != null) {
            this.mp3Url = other.getMp3Url();
        }

        if (other.getSpeaker() != null) {
            this.speaker = other.getSpeaker();
        }

        if(other.getDate() != null){
            this.date = other.getDate();
        }

        if(other.getImageUrl() != null){
            this.imageUrl = other.getImageUrl();
        }

        if(other.getLikes() != null){
            this.likes = other.getLikes();
        }

        if(other.getShares() != null){
            this.shares = other.getShares();
        }

        if(other.getPlays() != null){
            this.plays = other.getPlays();
        }

        if(other.getTags() != null && other.getTags().size() > 0){
            this.tags = other.getTags();
        }

        if(other.getSeries() != null){
            this.series = other.getSeries();
        }

        if(other.getPdfUrl() != null){
            this.pdfUrl = other.getPdfUrl();
        }

        if(other.getSlug() != null){
            this.slug = other.getSlug();
        }

        this.setLastUpdatedDate(LocalDate.now());

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SermonDBModel that = (SermonDBModel) o;

        if (!id.equals(that.id)) return false;
        if (!title.equals(that.title)) return false;
        if (!slug.equals(that.slug)) return false;
        if (!speaker.equals(that.speaker)) return false;
        if (!date.equals(that.date)) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        return series.equals(that.series);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + slug.hashCode();
        result = 31 * result + speaker.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SermonDBModel{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", slug='").append(slug).append('\'');
        sb.append(", mp3Url='").append(mp3Url).append('\'');
        sb.append(", videoUrl='").append(videoUrl).append('\'');
        sb.append(", speaker='").append(speaker).append('\'');
        sb.append(", date=").append(date);
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", slug='").append(slug).append('\'');
        sb.append(", likes=").append(likes);
        sb.append(", shares=").append(shares);
        sb.append(", plays=").append(plays);
        sb.append(", mapped=").append(mapped);
        sb.append(", tags=").append(tags);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", lastUpdatedBy='").append(lastUpdatedBy).append('\'');
        sb.append(", lastUpdatedDate=").append(lastUpdatedDate);
        //sb.append(", series=").append(series.getId());
        sb.append('}');
        return sb.toString();
    }
}
