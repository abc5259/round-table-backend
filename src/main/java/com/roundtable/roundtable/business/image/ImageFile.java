package com.roundtable.roundtable.business.image;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImageFile {

    private final MultipartFile file;

    private final String uniqueName;

    public ImageFile(MultipartFile file) {
        validateNullImage(file);
        validateImage(file);
        this.file = file;
        this.uniqueName = generateUniqueName(file);
    }

    private void validateNullImage(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateImage(final MultipartFile file) {
        if(!file.getContentType().startsWith("image")) {
            throw new IllegalArgumentException();
        }
    }

    private String generateUniqueName(final MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(fileName);

        return String.format("%s.%s",UUID.randomUUID(), extension);
    }

    public String getContentType() {
        return file.getContentType();
    }

    public long getSize() {
        return file.getSize();
    }

    public InputStream getInputStream() throws IOException {
        return file.getInputStream();
    }
}
