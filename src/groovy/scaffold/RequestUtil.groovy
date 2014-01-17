// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import java.io.InputStream
import javax.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile


/**
 * Helper functions for request stuff
 * @author jwarner
 *
 */
class RequestUtil {

    /**
     * Get inputstream for image from request
     * @param request
     * @return
     */
    public static InputStream selectInputStream(HttpServletRequest request, def params = null, max = null) {
        if (params?.qqfile && params.qqfile instanceof CommonsMultipartFile) {
            if (max) {
                if (params.qqfile.getSize() > max) {
                    return null
                }
                return params.qqfile.inputStream
            }
        }
        else if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')
            if (max) {
                if (uploadedFile.getSize() > max) {
                    return null
                }
            }
            return uploadedFile.inputStream
        }
        return request.inputStream
    }
}
