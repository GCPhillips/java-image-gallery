package edu.au.cc.gallery.data;

import java.util.List;

public interface ImageDAO {

    /**
     * @return return the list of images for a specific user
     */
    List<Image> getImages(User user) throws Exception;

    /**
     * @return gets the image (if it exists) based on the uuid
     */
    Image getImage(User user, String name) throws Exception;

    /**
     * Add an image
     */
    void addImage(User user, Image image) throws Exception;

    /**
     * Delete an image
     */
    void deleteImage(User user, Image image) throws Exception;
}
