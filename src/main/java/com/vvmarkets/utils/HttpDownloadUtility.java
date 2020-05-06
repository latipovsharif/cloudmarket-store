package com.vvmarkets.utils;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloadUtility {
    private static final int BUFFER_SIZE = 4096;
    public static String getProductSavePath() {
        return "media" + File.separator + "products";
    }

    public static String downloadFile(String fileURL, String fileName) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        String savePath = getSavePath(fileURL, fileName);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String contentType = httpConn.getContentType();
            if (!contentType.contains("image")) {
                return "";
            }

            try (InputStream inputStream = httpConn.getInputStream()) {
                try (FileOutputStream outputStream = new FileOutputStream(savePath)) {
                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        }

        httpConn.disconnect();
        return savePath;
    }

    public static String getSavePath(String sourcePath, String newFileName) {
        String ext = sourcePath.substring(sourcePath.lastIndexOf("."));
        return getProductSavePath() + File.separator + newFileName + ext;
    }

    public static Image getImagePathForProduct(String productID) {
        File file = new File("images" + File.separator + "no_image.png");;
        String[] supportedFormats = new String[]{".jpeg", ".jpg", ".png"};

        for (String format : supportedFormats) {
            String saveFilePath = getProductSavePath() + File.separator + productID + format;;
            File f = new File(saveFilePath);
            if(f.exists() && !f.isDirectory()) {
                file = new File(saveFilePath);
                break;
            }
        }

        return new Image(file.toURI().toString());
    }
}