// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

/**
 * Provides get and post methods for calling web service APIs (returning JSON)
 * @author jwarner
 *
 */
class HttpService {

    /**
     * Post the given string document to the given string url
     * @param urlAsString
     * @param doc
     * @return json string
     */
    def post(String urlAsString, String doc, proxy = null) {
        URL url
        Writer writer
        URLConnection connection

        log.error "Submitting document: "+doc

        url = new URL(urlAsString)

        if (proxy) connection = url.openConnection(proxy)
        else connection = url.openConnection()

        connection.setRequestMethod("POST")
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.doOutput = true

        writer = new OutputStreamWriter(connection.outputStream)
        writer.write(doc)
        writer.flush()
        writer.close()
        connection.connect()

        if (connection.responseCode == 200 || connection.responseCode == 201) {
            // success!
            def resp = connection.content.text
            return resp
        }
        else {
            // fail!
            log.error "ERROR POSTING DOCUMENT ${connection.responseCode}"
            log.error "RESPONSE: "+connection.getErrorStream().getText()
        }
    }

    /**
     * Do an HTTP get on the url with the params encoded as url params
     * @param url
     * @param params
     * @return json string
     */
    def get(String urlAsString, def params, proxy = null) {
        def resp
        def encodedParams = ""
        URL url
        URLConnection connection

        // Encoding the params...
        if (params) {
            params.each { k,v ->
                encodedParams += k.encodeAsURL() + '=' + v.encodeAsURL() + '&'
            }
            // take off the & from the end
            encodedParams = encodedParams[0..-2]
        }

        if (encodedParams) urlAsString += '?' + encodedParams

        log.error "Doing a GET to full URL: " + urlAsString
        url = new URL(urlAsString)
        if (proxy) connection = url.openConnection(proxy)
        else connection = url.openConnection()
        connection.setRequestMethod("GET")
        connection.connect()

        resp = connection.content.text
        log.error "Received from GET: " + resp

        return resp
    }
}
