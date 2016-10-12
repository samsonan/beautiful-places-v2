package com.samsonan.bplaces.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@Entity
@Table(name="places")
public class Place {

    @Id 
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotEmpty
    @Column(name="p_name")
    private String name;
    
    @NotEmpty
    @Column(name="description")
    private String description;
    
    @Min(-90)
    @Max(90)
    @Column(name="lat")
    private double latitude;

    @Min(-180)
    @Max(180)
    @Column(name="lon")
    private double longitude;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
    
    public Place(){
    }

    public Place(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public void addImage(Image image) {
        images.add( image );
        image.setPlace( this );
    }

    public void removePhone(Image image) {
        images.remove( image );
        image.setPlace( null );
    }    
    
    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
           .add("id", id)
           .add("name", name)
           .add("description", description)
           .add("latitude", latitude)
           .add("longitude", longitude)
           .add("images", images)
           .toString();
    }    
    
    //TODO: add additional attributes to compare
    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Place other = (Place) obj;
        return Objects.equal(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }
    
}
