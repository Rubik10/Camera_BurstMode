package com.rubik.appcameraburst.main.server;

/**
 * Created by Rubik on 21/9/16.
 */

public class AppCongif {

    private static final String PORT = "8888";
    private static final String SERVER = "/Android/serverProducts";
    private static final String URL_SERVER = "http://192.168.1.34:" + PORT + SERVER ;
    private static final String SERVER_UPLOAD = "/upload.php";

    public static final String UPLOAD_URL = URL_SERVER + SERVER_UPLOAD;  //"http://192.168.1.34:8888/Android/serverProducts/upload.php";
}
