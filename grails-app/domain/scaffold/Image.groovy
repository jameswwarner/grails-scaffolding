// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.EqualsBuilder


/**
 * Encapsulating image logic in Image Library
 * @author jwarner
 *
 */
class Image {
    String name
    String path                 // the path, not including bucket, on S3
    String sourceUrl            // the source (if any) before we copied to s3
    Long sizeInBytes            // the size in bytes of the file, used in deduplication
    Date created = new Date()

    static constraints = {
        name unique: true
        path nullable: true
        sourceUrl nullable: true
        sizeInBytes nullable: true
    }

    static transients = ['url']

    String getUrl() {
        def config = org.codehaus.groovy.grails.commons.ConfigurationHolder.config
        //TODO: Replace above deprecated use of ConfigurationHolder, see: http://burtbeckwith.com/blog/?p=993
        //New ways --> grails.util.Holders.config  OR  def grailsApplication and grailsApplication.config
        def prefix = config.aws.cloudfront.domain
        return prefix + '/' + path
    }

    @Override
    boolean equals(o) {
        if (this.is(o)) {
            return true
        }
        if (!(o instanceof Image)) {
            return false
        }
        return new EqualsBuilder().append(this.name, o.name).isEquals()
    }

    @Override
    int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.name).toHashCode()
    }

    static mapping = {
        datasources(['DEFAULT', 'readReplica'])
        //cache true
    }
}
