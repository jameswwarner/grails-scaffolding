// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import grails.converters.JSON
import org.springframework.web.context.request.RequestContextHolder


/**
 * Branched from Grails Facebook Graph plugin
 * http://www.grails.org/plugin/facebook-graph
 *
 */
class FacebookGraphService {

    // Injected by grails
    def grailsApplication
    def httpService

    boolean transactional = false

    /**
     * The domains that can be used in this class.
     */
    final static def DOMAIN_MAP = [
        api:'https://api.facebook.com/',
        api_read:'https://api-read.facebook.com/',
        graph:'https://graph.facebook.com/',
        www:'https://www.facebook.com/'
    ]

    /**
     * We use this call to request that facebook crawl the url
     * @param url
     * @return
     */
    def requestCrawl(def url) {
        def result

        try {
            result = api("", null, [id: url, scrape: 'true'], 'POST')
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result
    }

    /**
     * Get the facebook profile from the facebook id and access token
     * @param facebookUid
     * @param facebookAccessToken
     * @return
     */
    def getFacebookProfile(def facebookUid, def facebookAccessToken , params = [:]) {
        def result
        
        log.debug("Facebook data: ${facebookUid} ${facebookAccessToken}")

        try {
            result = api("/${facebookUid}", facebookAccessToken, params)
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result
    }

    /**
     * This method returns the information stored by Facebook of the session user.
     * If the session user hasn't associated a facebook session this method returns
     * null.
     */
    def getFacebookProfile(User user, params = [:]) {
        return getFacebookProfile(user.facebookUid, user.facebookAccessToken, params)
    }

    /**
     * Get the facebook id from a given url
     * @param user
     * @param url
     * @return
     */
    def getFacebookIdFromUrl(User user, String url) {
        def result

        log.debug("Facebook data: ${user.facebookUid} ${user.facebookAccessToken}")

        try {
            result = api("/", user.facebookAccessToken, [ids: url], 'GET')
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result ? result[url]?.id : null
    }

    /**
     * Publishes a comment to the given id
     * @param user
     * @param facebookId
     * @param comment
     * @return
     */
    def publishComment(User user, def facebookId, def comment) {
        def result

        log.debug("Facebook data: ${user.facebookUid} ${user.facebookAccessToken}")

        try {
            result = api("/${facebookId}/comments", user.facebookAccessToken, [message: comment], 'POST')
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result
    }

    /**
     * This method publishes the params passed as parameter in the
     * wall of the session user. If the session user hasn't associated
     * a facebook session this method returns null.
     * 
     * Thanks to mshirman.
     * 
     * @param params The map of params to publish. For instance
     * message, picture, link, name, caption, description
     */
    def publishWall(User user, params = [:]) {
        def result

        log.debug("Facebook data: ${user.facebookUid} ${user.facebookAccessToken}")

        try {
            result = api("/${user.facebookUid}/feed", user.facebookAccessToken, params, 'POST')
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result
    }

    /**
     * This method publishes the message passed as parameter in the
     * wall of the session user. If the session user hasn't associated
     * a facebook session this method returns null.
     *
     * @param message The message to publish
     */
    def publishWall(User user, String message) {
        return publishWall(user, message:message)
    }

    /**
     * Delete an action on facebook, taking the id
     * @param facebookUser
     * @param facebookId
     * @return
     */
    def deleteAction(User user, String facebookId) {
        def result

        log.debug("Deleting Facebook data: ${facebookId} ${user.facebookAccessToken}")

        try {
            result = api("/${facebookId}", user.facebookAccessToken, [:], 'DELETE')
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result
    }

    /**
     * This publishes open graph actions
     *
     * @param params The map of params to publish. For instance
     * message, picture, link, name, caption, description
     */
    def publishAction(User user, String actionName, params = [:]) {
        def result

        log.debug("Facebook data: ${user.facebookUid} ${user.facebookAccessToken}")

        try {
            result = api("/${user.facebookUid}/${actionName}", user.facebookAccessToken, params, 'POST')
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result
    }

    /**
     * This publishes open graph actions
     *
     * @param params The map of params to publish. For instance
     * message, picture, link, name, caption, description
     */
    def publishAction(User user, String actionName, String objectName, String objectUrl) {
        def result

        try {
            def params = [:]
            params[objectName] = objectUrl
            result = publishAction(user, actionName, params)
        } catch (Exception e) {
            log.error(e.message, e)
        }

        return result
    }

//    /**
//     * This method returns the String that can be used by an authorized user
//     * as the url of the profile image of the facebook user whose id is passed
//     * as parameter.
//     */
//    def getProfilePhotoSrc(facebookUserId) {
//        "${DOMAIN_MAP.graph}/${facebookUserId}/picture"
//    }

    /**
     * Get the profile photo url for the given Facebook Id
     * @param facebookId
     * @return
     */
    def getProfilePhotoUrl(def user, def facebookId) {
        return getProfilePhotoUrlWithToken(user.facebookAccessToken, facebookId)
    }

    /**
     * Get the profile photo url for the given Facebook Id
     * @param facebookId
     * @return
     */
    def getProfilePhotoUrlWithToken(def facebookAccessToken, def facebookId) {
        def result

        log.debug("Facebook data: ${facebookId} ${facebookAccessToken}")

        try {
            result = api("/${facebookId}/picture", facebookAccessToken, [type: 'large', redirect: 'false'], 'GET')
        } catch (Exception e) {
            log.error(e.message, e)
        }
        log.error result
        return result && result.data ? result.data.url : null
    }

    /**
     * Gets the cover image url for the given facebook id
     * @param user
     * @param facebookId
     * @return
     */
    def getCoverImageUrl(def user, def facebookId) {
        def result

        log.debug("Facebook data: ${user.facebookUid} ${user.facebookAccessToken}")

        try {
            result = api("/${facebookId}", user.facebookAccessToken, [type: 'large', redirect: 'false'], 'GET')
        } catch (Exception e) {
            log.error(e.message, e)
        }
        log.error "Result from FB: "+result
        return result && result.cover ? result.cover.source : null
    }

    /**
     * Invoke the Graph API.
     *
     * @param String $path the path (required)
     * @param String $method the http method (default 'GET')
     * @param Array $params the query/post data
     * @throws FacebookGraphException
     * @return the decoded response object
     */
    def api(path, accessToken, params = [:], method = 'GET') {
        def exception
        params.method = method // method override as we always do a POST

        def result = oauthRequest(getUrl('graph', path), params, accessToken)
        if (!result)
            throw new FacebookGraphException()
        else result = JSON.parse(result)

        log.debug("Result: ${result}")

        // results are returned, errors are thrown
        if (result.error) {
            throw new FacebookGraphException(result)
        }

        return result
    }

    /**
     * Make a OAuth Request
     *
     * @param path the path (required)
     * @param params the query/post data
     * @param the access token
     * @return the decoded response object
     */
    private def oauthRequest(url, params, accessToken) {
        if (!params['access_token'] && accessToken) {
            params['access_token'] = accessToken
        }

        // json_encode all params values that are not strings
        params.each{key,value ->
            if(!(value instanceof String)) params[key] = value as JSON
        }

        return makeRequest(url, params)
    }

    /**
     * Makes an HTTP request.
     *
     * @param urlAsString the URL to make the request to
     * @param params the parameters to use for the POST body
     * @return String the response text
     */
    private def makeRequest(urlAsString, params) {
        return httpService.get(urlAsString, params)
    }
 
    /**
     * Build the URL for given domain alias, path and parameters.
     *
     * @param name the name of the domain
     * @param path optional path (without a leading slash)
     * @param params optional query parameters
     * @return String the URL for the given parameters
     */
    private def getUrl(name, path = '', params = [:]) {
        def url = DOMAIN_MAP[name]
        if (path) {
            if (path[0] == '/') {
                path = path.substring(1)
            }
            url += path
        }
        if (params) {
            url += '?' 
            params.each{k,v->
                url += k.encodeAsURL + '=' + v.encodeAsURL()
            }
        }
        log.error("Calling Facebook with URL: "+url)
        return url
    }

    /**
    * It parses the signed request and it returns a json object with the signed params if the
    * sign is verify, and null otherwise.
    *
    * @see http://developers.facebook.com/docs/authentication/signed_request
    */
   private def parseSignedRequest(String signedRequest) {
       def result
       byte[] payloadSig
       String sig, payload
       String[] pair = signedRequest.split('\\.')
       
       if (pair.size() != 2) {
           log.warn "Signed request bad format: $signedRequest"
       } else {
           sig = unUrl(pair[0])
           payload = unUrl(pair[1])
           payloadSig = computePayloadSignature(payload)
           
           if (sig.decodeBase64() == payloadSig) {
               result = JSON.parse(new String(payload.decodeBase64()))
           }
       }
       return result
   }

   /**
    * It makes these replaces:
    * + by -
    * / by _
    */
   private String unUrl(String input) {
       return input.replace('_', '/').replace('-', '+')
   }

    /**
     * This method generates the signature of the payload passed as parameter,
     * using the Facebook application secret as secret for the HmacSHA256 algorithm.
     */
    byte[] computePayloadSignature(String payload) {
        String applicationSecret = grailsApplication.config.grails.plugins.springsecurity.facebook.secret
        Mac mac = Mac.getInstance('HmacSHA256')
        mac.init(new SecretKeySpec(applicationSecret.bytes, 'HmacSHA256'))
        return mac.doFinal(payload.bytes)
    }
}

public class FacebookGraphException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -5070446410644711801L;

    /**
     * The result from the API server that represents the exception information.
     */
    def result

    /**
     * The default constructor
     */
    public FacebookGraphException() {
        result = [:]
    }

    /**
     * Make a new API Exception with the given result.
     *
     * @param Array $result the result from the API server
     */
    public FacebookGraphException(result) {
        super(result['error'] ? result['error']['message'] : result['error_msg'])
        this.result = result
    }

    /**
     * Returns the associated type for the error. This will default to
     * 'Exception' when a type is not available.
     *
     * @return String
     */
    public def getType() {
        return (result['error'] && result['error']['type']) ? result['error']['type'] : 'Exception'
    }

    /**
     * To make debugging easier.
     *
     * @returns String the string representation of the error
     */
    public String toString() {
        return getType() + ': ' + getMessage()
    }
}

