package edu.au.cc.gallery.data;

import java.util.List;
import edu.au.cc.gallery.aws.*;
import edu.au.cc.gallery.ui.*;

public class S3ImageDAO implements ImageDAO {
    private String bucketBase = "edu.au.gcp0015.image-gallery";
    @Override
    public List<Image> getImages(User user) throws Exception {
        return null;
    }

    @Override
    public Image getImage(User user, String name) throws Exception {
        return null;
    }

    @Override
    public void addImage(User user, Image image) throws Exception {
        S3 s3 = new S3();
        try {
            s3.connect();
            if (Admin.getUserDAO().getUserByUsername(user.getUsername()) != null) {
                s3.putObject(bucketBase, image.getUuid(), image.getImageData(), "image/image");
                Admin.getUserDAO().addImage(user, image);
            }
        }
        catch (Exception ex) {
            System.out.println("[ERR]: " + ex.getMessage());
        }
    }

    @Override
    public void deleteImage(User user, Image image) throws Exception {

    }
}
