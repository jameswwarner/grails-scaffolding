// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.aws

import java.io.InputStream

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.AmazonClientException
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.ObjectMetadata


abstract class S3BaseService {
    static transactional = false

    def grailsApplication

    protected AmazonS3 s3client = null

    abstract protected initS3()

    boolean putFile(String bucket, String filename, File file) {
        if (!s3client) {
            initS3()
        }
        try {
            def result = s3client.putObject(bucket, filename, file)
        }
        catch (AmazonClientException ace) {
            // TODO: log out some stuff
            return false
        }
        return true;
    }

    boolean putFile(String bucket, String filename, InputStream inputStream) {
        putFile(bucket, filename, inputStream, null)
    }

    boolean putFile(String bucket, String filename, InputStream inputStream, String contentType) {
        if (!s3client) {
            initS3()
        }
        try {
            def objectMetadata = null
            if (contentType) {
                objectMetadata = new ObjectMetadata()
                objectMetadata.setContentType(contentType)
            }
            def result = s3client.putObject(bucket, filename, inputStream, objectMetadata)
        }
        catch (AmazonClientException ace) {
            // TODO: log out some stuff
            return false
        }
        return true
    }

    public InputStream getFile(String bucket, String filename) {
        if (!s3client) {
            initS3()
        }
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, filename)
        S3Object object = s3client.getObject(getObjectRequest)
        return object.objectContent
    }
}
