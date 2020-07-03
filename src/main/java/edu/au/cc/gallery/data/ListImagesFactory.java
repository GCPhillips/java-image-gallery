package edu.au.cc.gallery.data;

public class ListImagesFactory {
    public static ImageDAO getImageDAO() throws Exception { return new ListImageDAO(); }
}
