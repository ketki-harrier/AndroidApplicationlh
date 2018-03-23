package com.lifecyclehealth.lifecyclehealth.dto;

/**
 * Created by vaibhavi on 05-09-2017.
 */

public class SubmitProfileImageDTO {

    private String ImageData;
    private String FileName;
    private String ContentType;

    public String getImageData() {
        return ImageData;
    }

    public void setImageData(String imageData) {
        ImageData = imageData;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    @Override
    public String toString() {
        return "{" +
                "ImageData=" +ImageData +
                ", FileName='" + FileName + '\'' +
                ", ContentType='" + ContentType + '\'' +
                '}';
    }
}
