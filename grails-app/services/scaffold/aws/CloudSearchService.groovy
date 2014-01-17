// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.aws

import groovy.json.JsonSlurper
import grails.converters.JSON
import java.net.Proxy


//import com.amazonaws.services.cloudsearch.AmazonCloudSearch
//import com.amazonaws.services.cloudsearch.AmazonCloudSearchClient
//import com.amazonaws.auth.BasicAWSCredentials

/**
 * This is a wrapper around the CloudSearch API stuff.
 * @author jwarner
 *
 */
class CloudSearchService {
    static transactional = false

    static def docServiceResource = '/2011-02-01/documents/batch'
    static def searchServiceResource = '/2011-02-01/search'

    def httpService
    def grailsApplication

    // API doesn't currently support adding documents
//    AmazonCloudSearch amazonCloudSearch
//
//    def init() {
//        def credentials = new BasicAWSCredentials(grailsApplication.config.aws.accessKey,
//            grailsApplication.config.aws.secretKey)
//        amazonCloudSearch = new AmazonCloudSearchClient(credentials)
//    }

    private def getProxy() {
        def proxyHost = grailsApplication.config.aws.cloudsearch.proxyHost
        def proxyPort = grailsApplication.config.aws.cloudsearch.proxyPort
        if (proxyHost) {
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort))
        }
        else {
            return null
        }
    }

    /**
     * Generic index function taking id, version, and object
     * @param id
     * @param version
     * @param object
     * @return
     */
    def index(def id, def version, def object) {
        def url = grailsApplication.config.aws.cloudsearch.doc.domain + docServiceResource
        def dataMap = [type: "add", id: id, version: version, lang: 'en', fields: object]
        List items = [dataMap]

        def response = httpService.post(url, (items as JSON).toString(), getProxy())
        log.error response
    }

    /**
     * Submit the product to Amazon to be indexed
     * @param product
     * @return
     */
    def index(def product) {
        // DEVNOTE: Amazon only allows field names with lowercase letters and underscores. No camel case!
        def object = [name: product.name, description: product.description ? product.description : '',
            brand: product.brand.name, brand_alternate_names: product.brand.alternateNames ? product.brand.alternateNames : '',
            product_items: product.productItems*.name,
            is_prestige: product.brand.prestige ? 1 : 0,
            taxonomies: product.taxonomyPaths.size()>0 ? product.taxonomyPaths : '',
            product_options: product.productItems*.productOption.name.join(' '),
            num_brand_followers: product.brand.brandFollows.size(),
            num_vanities: product.productItems*.shelfItems.flatten().size(),
            num_reasons: (product.productItems*.shelfItems*.reason.flatten() - null).size(),
            reasons: product.productItems*.shelfItems*.reason.flatten().join(' '),
            keywords: product.keywords.join(' ')]
        //println object as JSON
        return index(product.id, product.version, object)
    }

    /**
     * Delete the doc with the given id and version
     * @param id
     * @param version
     * @return
     */
    def deleteFromIndex(def id, def version) {
        def url = grailsApplication.config.aws.cloudsearch.doc.domain + docServiceResource
        def dataMap = [type: "delete", id: id, version: version]
        List items = [dataMap]

        def response = httpService.post(url, (items as JSON).toString(), getProxy())
        log.error response
    }

    /**
     * Delete the given product from the index
     * @param product
     * @return
     */
    def deleteFromIndex(def product) {
        return deleteFromIndex(product.id, product.version+1)
    }

    /**
     * Search amazon for the given query
     * @param query
     * @return
     */
    def search(def query, def start = null, def size = null) {
        def url = grailsApplication.config.aws.cloudsearch.search.domain + searchServiceResource
        def params = [:]
        // take out any words with gremlins
        query = query.replaceAll(/\s+[a-zA-Z0-9]*[^a-zA-Z0-9\s][a-zA-Z0-9]*\s+/, ' ')
        log.debug("QUERY to cloudsearch: "+query)

        // just a straight query
        params['q'] = query

        // pagination parameters
        if (start) {
            params['start'] = start
        }
        if (size) {
            params['size'] = size
        }

        // set ranking expression
        def ranking = grailsApplication.config.aws.cloudsearch.rankingExpression
        params['rank'] = "-${ranking}"

        def slurper = new JsonSlurper()
        String result = "[]"
        if (params.q || params.bq)
           result = httpService.get(url, params)
        return slurper.parseText(result)
    }
}

