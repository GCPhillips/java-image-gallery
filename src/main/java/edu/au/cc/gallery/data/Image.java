package edu.au.cc.gallery.data;

public class Image {
    private String name;
    private User user;
    private byte[] imageData;
    private String uuid;


    public Image(User user, String name, byte[] imageData) {
        this.user = user;
        this.name = name;
        this.imageData = imageData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }
}
