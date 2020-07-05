package edu.au.cc.gallery.data;

public class ListImageFactory {
    public static ImageDAO getImageDAO() throws Exception { return new ListImageDAO(); }
}
