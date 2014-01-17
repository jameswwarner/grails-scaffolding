// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold


class TokenService {
    String cookieName = "token"
    int cookieExpiration = 10368000  //4 months

    def cookieService

    String newCookie(response) {
        def token = java.util.UUID.randomUUID().toString()
        cookieService.set(cookieName, token, cookieExpiration)
        return token
    }

    String getCookie() {
        return cookieService.get(cookieName)
    }

    void deleteCookie() {
        cookieService.delete(cookieName)
    }

    /**
     * When a user logs in, we associate the token with the user (and, if needed,
     *  disassociate this token from another user)
     * @param user
     * @param token
     * @return
     */
    def updateUserToken(def user, def token) {
        def userToken = UserToken.findByUserToken(token)
        if (!userToken) {
            userToken = new UserToken(userToken: token, user: user)
        }
        else {
            userToken.user = user
        }
        user.addToUserTokens(userToken)
        userToken.save()
        user.save()
    }

    /**
     * Setup a remembered user into the current session
     * @param request
     * @param session
     * @return
     */
    def setupSessionUser(def request, def session) {
        def user = User.get(session.userId)
        if (!user) {
            user = getRememberedUser(request.token)
            if (user) {
                session.userId = user.id
                recordSession(user, request, session)
            }
        }
        return user
    }

    /**
     * Use the token to look up the user
     * @param userToken
     * @return
     */
    def getRememberedUser(String token) {
        def userToken = UserToken.findByUserToken(token)
        if (userToken) {
            def user = userToken.user
            def now = new Date()
            if (!user.facebookUid || user.facebookTokenExpires > now) {
                return user
            }
        }
        return null
    }

    /**
     * Record this user session - when a user comes to the site
     * @param request
     * @return
     */
    def recordSession(def user, def request, def session) {
        def userAgentStr = StringUtil.limitString(request.getHeader("User-Agent"), User.defaultMaxStringLen)
        def referer = StringUtil.limitString(session.referer, User.defaultMaxStringLen)

        def userVisit = new UserVisit(user: user, referer: referer, userAgent: userAgentStr, userToken: request.token)
        userVisit.save(insert: true)
    }
}

