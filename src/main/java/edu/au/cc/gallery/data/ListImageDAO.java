package edu.au.cc.gallery.data;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class ListImageDAO implements ImageDAO {
    private HashMap<User, ArrayList<Image>> images;

    public ListImageDAO() {
        images = new HashMap<>();
    }

    @Override
    public List<Image> getImages(User user) throws Exception {
        return images.get(user);
    }

    @Override
    public Image getImage(User user, String name) throws Exception {
        var userImages = images.get(user);
        for (Image image: userImages) {
            if (image.getUuid().equals(name))
                return image;
        }
        return null;
    }

    @Override
    public void addImage(User user, Image image) throws Exception {
        var userImages = images.get(user);
        if (userImages != null && !userImages.contains(image)) {
            userImages.add(image);
        }
    }

    @Override
    public void deleteImage(User user, Image image) throws Exception {
        var userImages = images.get(user);
        if (userImages != null && userImages.contains(image)) {
            userImages.remove(image);
        }
    }
}
