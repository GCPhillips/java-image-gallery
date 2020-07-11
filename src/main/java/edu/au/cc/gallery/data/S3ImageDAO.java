package edu.au.cc.gallery.data;

import java.util.List;
import java.util.ArrayList;
import edu.au.cc.gallery.aws.*;
import edu.au.cc.gallery.ui.*;

public class S3ImageDAO implements ImageDAO {
    private static String bucketname;

    public static void setBucketname(String bucketName) {
        bucketname = bucketName;
    }

    @Override
    public List<Image> getImages(User user) throws Exception {
        S3 s3 = new S3();
        List<Image> images = new ArrayList<>();
        try {
            s3.connect();
            List<String> uuids = Admin.getUserDAO().getImageUuids(user);
            for (String uuid: uuids) {
                String imagedata = s3.getObject(bucketname, uuid);
                Image image = new Image(user, uuid, imagedata);
                images.add(image);
            }
        }
        catch (Exception ex) {
            System.out.println("[ERR][S3ImageDAO.getImages()]: " + ex.getMessage());
        }

        return images;
    }

    @Override
    public Image getImage(User user, String uuid) throws Exception {
        S3 s3 = new S3();
        try {
            s3.connect();
            String imagedata = s3.getObject(bucketname, uuid);
            Image image = new Image(user, uuid, imagedata);
            return image;
        }
        catch (Exception ex) {
            System.out.println("[ERR][S3ImageDAO.getImage()]: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void addImage(User user, Image image) throws Exception {
        S3 s3 = new S3();
        if (user == null || image == null)
            return;
        try {
            s3.connect();
            if (Admin.getUserDAO().getUserByUsername(user.getUsername()) != null) {
                s3.putObject(bucketname, image.getUuid(), image.getImageData(), "image/image");
                Admin.getUserDAO().addImage(user, image);
            }
        }
        catch (Exception ex) {
            System.out.println("[ERR][S3ImageDAO.addImage()]: " + ex.getMessage());
        }
    }

    @Override
    public void deleteImage(User user, Image image) throws Exception {
        S3 s3 = new S3();
        if (user == null || image == null)
            return;
        try {
            s3.connect();
            if (Admin.getUserDAO().getUserByUsername(user.getUsername()) != null) {
                s3.deleteObject(bucketname, image.getUuid());
                Admin.getUserDAO().deleteImage(user, image);
            }
        }
        catch (Exception ex) {
            System.out.println("[ERR][S3ImageDAO.deleteImage()]: " + ex.getMessage());
        }
    }
}
