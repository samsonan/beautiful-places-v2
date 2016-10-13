package com.samsonan.bplaces.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@Entity
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    //TODO: @NaturalId ??? 
    @NotNull
    @Column(name="filename")
    private String filename;

    @NotEmpty
    @Column(name="title")
    private String title;

    @NotEmpty
    @Column(name="description")
    private String description;

    @Column(name="author")
    private String author;
    
    @NotEmpty
    @Column(name = "content_type")
    private String contentType;
    
    @Column(name = "created")
    private Date created;
    
    @Column(name = "updated")
    private Date updated;

    public int getOrdenal() {
        return ordenal;
    }

    public void setOrdenal(int ordenal) {
        this.ordenal = ordenal;
    }

    @Column(name = "ordenal")
    private int ordenal;
    
    @ManyToOne
    private Place place;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String autor) {
        this.author = autor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
           .add("id", id)
           .add("title", title)
           .add("author", author)
           .add("description", description)
           .add("path", filename)
           .add("contentType", contentType)
           .add("created", created)
           .add("updated", updated)
           .toString();
    }    
    
    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Image other = (Image) obj;
        return Objects.equal(this.title, other.title)
                && Objects.equal(this.filename, other.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.title, this.filename);
    }
    
}
