package com.roundtable.roundtable.business.image;

import static com.roundtable.roundtable.global.exception.errorcode.ImageErrorCode.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.roundtable.roundtable.global.exception.CoreException;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AWSS3ImageUploader implements ImageUploader {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.folder}")
    private String folder;

    @Override
    public String upload(MultipartFile file) {
        ImageFile imageFile = new ImageFile(file);
        return uploadImage(imageFile);
    }

    private String uploadImage(final ImageFile imageFile) {
        final String path = folder + imageFile.getUniqueName();
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());

        try (final InputStream inputStream = imageFile.getInputStream()) {
            s3Client.putObject(bucket, path, inputStream, metadata);
        } catch (final AmazonServiceException | IOException e) {
            throw new CoreException(UPLOAD_ERROR, e);
        }
        return imageFile.getUniqueName();
    }
}
