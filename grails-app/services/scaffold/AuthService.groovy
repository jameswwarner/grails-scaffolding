// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold


/**
 * For all of our friendly authentication stuff.
 * @author jwarner
 *
 */
class AuthService {

    // sets up which pages are admin only, and which are open
    // anything not in either of these two is authenticated users only
    final securityMap = [
        open: [
           auth:               ['login-facebook', 'email-register', 'forgot-password', 'reset-password',
                                'email-login'],
           signup:             ['index'],
           user:               ['index'],
           eventLog:           ['*']
        ],
        admin: [
           dataHub:          ['*'],
           mktgHub:          ['*'],
           opsHub:           ['*'],
           configuration:    ['add', 'update', 'list', 'list-editable'],
           adminManage:      ['*'],
           jobTrigger:       ['*']
        ]
    ]

    final openUserAgents = ['Googlebot', 'http://www.google.com/bot.html',
        'facebookexternalhit', 'http://www.facebook.com/externalhit_uatext.php']

    /**
     * Do you need to be an admin to view this page?
     * @param controllerName
     * @param actionName
     * @return
     */
    def pageAdminRestricted(String controllerName, String actionName) {
        return securityMap.admin.get(controllerName)?.contains(actionName) ||
            securityMap.admin.get(controllerName)?.contains('*')
    }

    /**
     * Is this an open page?
     * @param controllerName
     * @param actionName
     * @return
     */
    def pageOpen(String controllerName, String actionName) {
        return securityMap.open.get(controllerName)?.contains(actionName) ||
            securityMap.open.get(controllerName)?.contains('*')
    }

    /**
     * Is this user an admin?
     * @param user
     * @return
     */
    def isAdmin(User user) {
        return user?.roles?.contains(Role.admin)
    }

    /**
     * Returns true if you have to be an admin to view this page, but you're not one
     * @param controllerName
     * @param actionName
     * @param user
     * @return
     */
    def accessDeniedAdmin(String controllerName, String actionName, User user) {
        return pageAdminRestricted(controllerName, actionName) && !isAdmin(user)
    }

    /**
     * Returns true if this is a page that should be open because of an explicitly allowed user agent
     * @param userAgent
     * @return
     */
    def openUserAgent(String userAgent) {
        for (def openUserAgent : openUserAgents) {
            if (userAgent?.contains(openUserAgent)) {
                return true
            }
        }
        return false
    }

    /**
     * Returns true if the page is explicitly opened
     * @param controllerName
     * @param actionName
     * @param user
     * @param request
     * @return
     */
    def accessOpen(String controllerName, String actionName, User user, def request) {
        return pageOpen(controllerName, actionName) || (user && user.enabled)
    }

    /**
     * Main driver function called in userFilters for rest calls. Also used as
     *  sub call for pageAccessDenied.
     * @param controllerName
     * @param actionName
     * @param user
     * @param request
     * @return
     */
    def accessDenied(String controllerName, String actionName, User user, def request) {
        return accessDeniedAdmin(controllerName, actionName ?: 'index', user) ||
               !accessOpen(controllerName, actionName ?: 'index', user, request)
    }

    /**
     * Is this a page view open because of first click free?
     * @param controllerName
     * @param actionName
     * @param user
     * @param request
     * @return
     */
    def firstClickFree(String controllerName, String actionName, User user, def request) {
        return !request.getHeader('referer')?.contains('scaffold.com')
    }

    /**
     * Main driver for page calls - is this an allowed view?
     * @param controllerName
     * @param actionName
     * @param user
     * @param request
     * @return
     */
    def pageAccessDenied(String controllerName, String actionName, User user, def request) {
        return accessDenied(controllerName, actionName ?: 'index', user, request) &&
               !openUserAgent(request.getHeader("User-Agent")) &&
               !firstClickFree(controllerName, actionName ?: 'index', user, request)
    }

}
