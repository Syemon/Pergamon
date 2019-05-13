package com.pergamon.Pergamnon.v1.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="file")
public class File {
    @Id
    @GeneratedValue()
    private UUID id;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="storage_name")
    private String storageName;

    @NotNull
    @Column(name="type")
    private String type;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File setName(String name) {
        this.name = name;

        return this;
    }

    public String getStorageName() {
        return storageName;
    }

    public File setStorageName(String storageName) {
        this.storageName = storageName;

        return this;
    }

    public String getType() {
        return type;
    }

    public File setType(String type) {
        this.type = type;

        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
