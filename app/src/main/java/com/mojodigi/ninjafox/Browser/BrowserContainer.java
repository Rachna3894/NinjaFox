package com.mojodigi.ninjafox.Browser;

import com.mojodigi.ninjafox.View.jmmWebView;

import java.util.LinkedList;
import java.util.List;



public class BrowserContainer {
    private static List<AlbumController> list = new LinkedList<>();

    public static AlbumController get(int index) {
        return list.get(index);
    }

    public synchronized static void set(AlbumController controller, int index) {

        try {
            if (list.get(index) instanceof jmmWebView) {
                ((jmmWebView) list.get(index)).destroy();
            }

            list.set(index, controller);
        }catch (Exception e)
        {

        }
    }

    public synchronized static void add(AlbumController controller) {
        list.add(controller);
    }

    public synchronized static void add(AlbumController controller, int index) {
        list.add(index, controller);
    }

    public synchronized static void remove(int index) {
        if (list.get(index) instanceof jmmWebView) {
            ((jmmWebView) list.get(index)).destroy();
        }

        list.remove(index);
    }

    public synchronized static void remove(AlbumController controller) {
        if (controller instanceof jmmWebView) {
            ((jmmWebView) controller).destroy();
        }

        list.remove(controller);
    }

    public static int indexOf(AlbumController controller) {
        return list.indexOf(controller);
    }

    public static List<AlbumController> list() {
        return list;
    }

    public static int size() {
        return list.size();
    }

    public synchronized static void clear() {
        for (AlbumController albumController : list) {
            if (albumController instanceof jmmWebView) {
                ((jmmWebView) albumController).destroy();
            }
        }

        list.clear();
    }
}
