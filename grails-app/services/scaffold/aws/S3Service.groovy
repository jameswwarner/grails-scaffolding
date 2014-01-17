// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.aws

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.ClientConfiguration
import java.net.InetAddress
import scaffold.StringUtil


/**
 * Actual implementation for S3 service.
 * @author jwarner
 *
 */
class S3Service extends S3BaseService {
    static transactional = false

    // auto-configured directories
    def imageDirectory = "images"

    @Override
    protected initS3() {
        def credentials = new BasicAWSCredentials(grailsApplication.config.aws.accessKey,
                grailsApplication.config.aws.secretKey)
        def configuration = new ClientConfiguration()
        configuration.setConnectionTimeout(240 * 1000)
        s3client = new AmazonS3Client(credentials, configuration)
    }

    /**
     * Add an image to our S3 bucket
     * @param extension
     * @param inputStream
     * @return filename if success, null if failure
     */
    public String addImage(String extension, InputStream inputStream) {
        def bucket = grailsApplication.config.aws.bucket.assets
        String randomString = StringUtil.getRandomString(12)

        def filename = imageDirectory+'/'+randomString+'.'+extension
        def contentType = null
        if (extension == "jpg" || extension == "jpeg") {
            contentType = "image/jpeg"
        }
        else if (extension == "png") {
            contentType = "image/png"
        }
        else if (extension == "gif") {
            contentType = "image/gif"
        }
        if (!putFile(bucket, filename, inputStream, contentType)) {
            // error
            return null
        }
        return filename
    }

    String getDateDirName() {
        return new Date().format("yyyy/MM/dd/");
    }

    public boolean addLog(File file) {
        def bucket = grailsApplication.config.aws.bucket.logs
        def host = InetAddress.getLocalHost().hostName
        def filename = getDateDirName() + host+"_"+file.getName()
        if (!putFile(bucket, filename, file)) {
            // error
            return false
        }
        return true
    }
}
