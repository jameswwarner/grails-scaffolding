// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import scaffold.Configuration
import scaffold.User


/**
 * The scaffold-specific tag lib for our gsp pages.
 * @author jwarner
 *
 */
class CoreTagLib {

    def authService
    def propertiesService

    def property = { attrs, body ->
        out << propertiesService[attrs.name]
    }

    def configuration = { attrs, body ->
        out << Configuration.lookup(attrs.name)
    }

    def username = { attrs, body ->
        out << (User.get(session.userId)?.username)
    }

    def userDisplayName = { attrs, body ->
        out << (User.get(session.userId)?.firstName)
    }

    def facebookId = { attrs, body ->
        out << (User.get(session.userId)?.facebookUid)
    }

    def ifAdmin = { attrs, body ->
        if (authService.isAdmin(User.get(session.userId)))
            out << body()
    }

    def ifEnabled = { attrs, body ->
        if (User.get(session.userId)?.enabled) {
            out << body()
        }
    }

    def ifNotEnabled = { attrs, body ->
        if (!User.get(session.userId)?.enabled) {
            out << body()
        }
    }

    def ifFacebookUser = { attrs, body ->
        if (User.get(session.userId)?.facebookUid) {
            out << body()
        }
    }

    def ifNotFacebookUser = { attrs, body ->
        if (!User.get(session.userId)?.facebookUid) {
            out << body()
        }
    }
}
