// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.page

import scaffold.Configuration;


/**
 * Controller for displaying pages.
 * @author jwarner
 *
 */
class PageController {

    def pageService
    def propertiesService
    def systemService

    def index() {
        redirect uri: "/"
    }

    def gateway() {
    }

    def about() {
    }

    def team() {
    }

    def jobs() {
    }

    def investors() {
    }

    def privacy() {
    }

    def terms() {
    }

    def contact() {
    }

    def notFound() {
    }

    def error() {
    }

    def maintenance() {
    }

    def robots() {
    }

    def images() {
        def folderName = "/images/key/"
        def folder = systemService.getFile(folderName)
        def start = folder.toString().size() - folderName.size() + 1
        def images = []
        folder.eachFileRecurse(groovy.io.FileType.FILES) {
            if (it.toString().endsWith(".png") || it.toString().endsWith(".jpg"))
               images.add(it.toString().substring(start))
        }
        [folderName: folderName, images: images]
    }

}
