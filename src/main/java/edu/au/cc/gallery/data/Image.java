package edu.au.cc.gallery.data;

public class Image {
    private String uuid;
    private User user;
    private String image;


    public Image(User user, String uuid, String image) {
        this.user = user;
        this.uuid = uuid;
        this.image = image;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
