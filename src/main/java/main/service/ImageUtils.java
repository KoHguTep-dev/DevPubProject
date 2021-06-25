package main.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUtils {

    public static String upload(MultipartFile image) {
        String ext = FilenameUtils.getExtension(image.getOriginalFilename());
        byte[] bytes = getBytes(image, ext);
        if (bytes != null) {
            return getPathAndWrite(ext, bytes);
        }
        return null;
    }

    public static String resizeUpload(MultipartFile image) {
        String ext = FilenameUtils.getExtension(image.getOriginalFilename());
        byte[] bytes = getBytes(image, ext);
        if (bytes != null) {
            try {
                BufferedImage newImage = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes)), 36, 36);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                ImageIO.write(newImage, ext, bs);
                byte[] newBytes = bs.toByteArray();
                return getPathAndWrite(ext, newBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static byte[] getBytes(MultipartFile image, String ext) {
        if (image.isEmpty())
            return null;
        if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png")) {
            try {
                byte[] bytes = image.getBytes();
                if (bytes.length > 5242880) {
                    return null;
                }
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getPathAndWrite(String ext, byte[] bytes) {
        StringBuilder path = new StringBuilder();
        String random = RandomStringUtils.randomAlphabetic(6);
        String letters = random.toLowerCase();
        String numbers = RandomStringUtils.randomNumeric(10);

        path.append("upload/")
                .append(letters.substring(0, 2)).append("/")
                .append(letters.substring(2, 4)).append("/")
                .append(letters.substring(4)).append("/")
                .append(numbers).append(".").append(ext);
        Path file = Paths.get(path.toString());
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/" + path;
    }

}
