package com.org.cafemgmt.service;

import java.io.File;
import java.net.URL;

public interface SaveToS3Service {
    public URL saveImageToS3(File image, String imageKey);
}
