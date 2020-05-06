package com.vvmarkets.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility that downloads a file from a URL.
 * @author www.codejava.net
 *
 */
public class HttpDownloadUtility {
    private static final int BUFFER_SIZE = 4096;
    public static String getProductSavePath() {
        return "media" + File.separator + "products";
    }

    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @throws IOException
     */
    public static String downloadFile(String fileURL, String fileName) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        String savePath = getSavePath(fileURL, fileName);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String contentType = httpConn.getContentType();
            if (!contentType.contains("image")) {
                return "";
            }
                // extracts file name from URL

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(savePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
        }

        httpConn.disconnect();
        return savePath;
    }

    public static String getSavePath(String sourcePath, String newFileName) {
        String saveFilePath = "";
        String ext = sourcePath.substring(sourcePath.lastIndexOf("."));

        saveFilePath = getProductSavePath() + File.separator + newFileName + ext;

        return saveFilePath;
    }

    public static String getImagePathForProduct(String productID) {
        List<String> supportedFormats = new ArrayList<String>();
        supportedFormats.add(".jpeg");
        supportedFormats.add(".jpg");
        supportedFormats.add(".png");

        for (String format : supportedFormats) {
            String saveFilePath = getProductSavePath() + File.separator + productID + format;;
            File f = new File(saveFilePath);
            if(f.exists() && !f.isDirectory()) {
                return saveFilePath;
            }
        }

        return "images" + File.separator + "no_image.png";
    }
}