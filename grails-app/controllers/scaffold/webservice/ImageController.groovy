// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.webservice

import scaffold.Image
import scaffold.User
import scaffold.RequestUtil
import scaffold.RestUtil
import scaffold.StringUtil
import scaffold.WebserviceError

import grails.converters.JSON
import com.sun.tools.javac.util.Log;
import org.imgscalr.Scalr
import javax.imageio.ImageIO


/**
 * Controller for creating/updating/etc images
 * @author jwarner
 *
 */
class ImageController {

    def imageService
    def userAgentIdentService

    /**
     * Default is just a get
     * @return
     */
    def index() {
        def resource = Image.get(params.id)
        if (!resource)
            resource = WebserviceError.resourceNotFound
        render resource as JSON
    }

    /**
     * Upload a new image
     */
    def upload() {
        // get everything after last '.'
        def extension = request['javax.servlet.forward.query_string'].split("\\.")[-1]
        def name = request['javax.servlet.forward.query_string'].split("\\.")[0]
        name = name.substring("qqfile=".length()) + ' ' + (new Date()).format("yyyyMMddkkmmss")
        log.error "Name: ${name} Extension: ${extension}"
        InputStream inputStream = RequestUtil.selectInputStream(request, params)
        // load the file to s3 and get the path
        def image = imageService.createFromInputStream(extension, name, inputStream)
        if (!image) {
            if (userAgentIdentService.isMsie()) {
                return render(text: [success:false] as JSON, contentType:'text/plain')
            }
            else {
                return render(text: [success:false] as JSON, contentType:'text/json')
            }
        }
        if (userAgentIdentService.isMsie()) {
            return render(text: [success:true, image:image] as JSON, contentType:'text/plain')
        }
        else {
            return render(text: [success:true, image:image] as JSON, contentType:'text/json')
        }
    }

    /**
     * Takes the upload, scales it to the appropriate size, and saves it to S3.
     * Output is different from usual because of plugin.
     * @return
     */
    def avatarUpload() {
        try {
            def user = User.get(session.userId)
            InputStream inputStream = RequestUtil.selectInputStream(request, params, 1024000)

            def filename
            if (params.qqfile instanceof org.springframework.web.multipart.commons.CommonsMultipartFile) {
                filename = params.qqfile.originalFilename
            }
            else {
                filename = request['javax.servlet.forward.query_string'] ?: params.qqfile
            }

            log.error "Uploaded file name: "+filename
            log.error "qqfile: "+params.qqfile
            def extension = filename.split("\\.")[-1]
            log.error "Extension of uploaded file name: "+extension
            def dateStr = (new Date()).format("yyyyMMddkkmmss")
            def name = user.username + "-avatar-" + dateStr

            def rawImageBuffer = ImageIO.read(inputStream)
            def scaledImage = Scalr.resize(rawImageBuffer, 360)

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, extension, os);
            InputStream scaledIs = new ByteArrayInputStream(os.toByteArray());
            def image = imageService.createFromInputStream(extension, name, scaledIs)
            if (userAgentIdentService.isMsie()) {
                log.error "Successfully loaded image from IE: "+image.id
                return render(contentType:'text/html', text: [success:true, image:image] as JSON)
            }
            else {
                log.error "Successfully loaded image: "+image.id
                render ([success:true, image:image] as JSON)
            }
        }
        catch (Exception e) {
            if (userAgentIdentService.isMsie()) {
                log.error "Exception uploading image from IE: ", e
                return render(text: [success:false, exception:WebserviceError.imageUploadError] as JSON, contentType:'text/plain')
            }
            else {
                log.error "Exception uploading image: ", e
                render ([success:false, exception:WebserviceError.imageUploadError] as JSON)
            }
        }
    }

    /**
     * To be used instead of upload, this you give a url to an image on another site
     */
    def create() {
        def url = params.url
        //log.error "Loading image from "+url
        def image = imageService.createFromRemote(url)
        if (!image) {
            return render(text: [success:false] as JSON, contentType:'text/json')
        }
        return render(text: [success:true, image:image] as JSON, contentType:'text/json')
    }

    /**
     * Update the name on an image
     */
    def update() {
        def resource = Image.get(params.id)
        if (!resource) {
            resource = WebserviceError.resourceNotFound
        }
        else {
            RestUtil.setFieldsMap(resource, params, [name: "name"])
            resource.save()
        }
    }

    /**
     * This will just list the most recently uploaded images.
     * You will also be able to search tags and titles.
     */
    def list() {
        def max = params.max ? params.max.toInteger() : 5
        def resource = Image.findAll("from Image as i order by i.created desc", [max: max])
        render resource as JSON
    }

    /**
     * Text search through names
     */
    def search() {
        //println "Doing search: "+params.query;
        if (!params.query) {
            // no results, should just return empty something
            render ([]) as JSON
            return
        }

        def query = Image.where {
            name =~ "%"+params.query+"%"
        }
        def imageResults = query.list()

        def newImages = Image.findAllByTag(params.query)
        imageResults.addAll(newImages)

        render imageResults as JSON
    }

}
