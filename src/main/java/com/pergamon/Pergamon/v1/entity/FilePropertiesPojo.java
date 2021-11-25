package com.pergamon.Pergamon.v1.entity;

public class FilePropertiesPojo {
    private String name;
    private String storageName;
    private String type;

    public String getName() {
        return name;
    }

    public FilePropertiesPojo setName(String name) {
        this.name = name;

        return this;
    }

    public String getStorageName() {
        return storageName;
    }

    public FilePropertiesPojo setStorageName(String storageName) {
        this.storageName = storageName;

        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
