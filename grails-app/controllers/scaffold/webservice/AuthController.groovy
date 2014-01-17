// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.webservice

import grails.converters.JSON
import groovy.time.*

import scaffold.User
import scaffold.StringUtil
import scaffold.WebserviceError


/**
 * This is the REST WS for interacting with users
 * @author jwarner
 *
 */
class AuthController {

    def facebookGraphService
    def userService
    def tokenService
    def securityService

    /**
     * Handle facebook login.
     * @return
     */
    def loginFacebook() {
        def accessToken   = params.accessToken
        def expiresIn     = params.expiresIn
        def signedRequest = params.signedRequest
        def userId        = params.userID

        //log.error "accessToken: ${accessToken} | expiresIn: ${expiresIn} | signedRequest: ${signedRequest} | userId: ${userId}"

        //TODO: should we verify the signed request?
        //  Here or in JS? https://developers.facebook.com/docs/howtos/login/signed-request/

        // lookup if there is an existing user
        def user = User.findByFacebookUid(userId)

        if (user && user.accountLocked) {
            render WebserviceError.notAuthorized as JSON
        }

        def expirationDate = new TimeDuration( 0, 0, expiresIn.toInteger(), 0 ) + new Date()

        def newUser = false
        if (!user) {
            // make a new user
            def profile = facebookGraphService.getFacebookProfile(userId, accessToken)
            log.debug "Profile: ${profile}"
            def mobileDeviceId = null
//            withMobileDevice { def device ->
//                mobileDeviceId = device.id
//            }
//            if (mobileDeviceId && mobileDeviceId.size() > User.defaultMaxStringLen) {
//                mobileDeviceId = mobileDeviceId[0..User.defaultMaxStringLen-1]
//            }
            def userAgentStr = StringUtil.limitString(request.getHeader("User-Agent"), User.defaultMaxStringLen)
            def referer = StringUtil.limitString(session.referer, User.defaultMaxStringLen)
            def enabled = true
            def username = profile.username
            if (!username || User.findByUsername(username)) {
                username = "${profile.first_name}.${profile.last_name}.f${userId}".toString()
            }
            user = User.findByEmail(profile.email) // may have previously registered with email
            if (!user) {
                newUser = true
                user = new User(email: profile.email, facebookUid: userId, facebookAccessToken: accessToken,
                    facebookTokenExpires: expirationDate, username: username.toLowerCase(), firstName: profile.first_name,
                    lastName: profile.last_name, referer: referer, mobileDeviceId: mobileDeviceId,
                    userAgent: userAgentStr, enabled: enabled, emailConfirmed: new Date())
                //userService.setProfileImage(user)
                user.save()
            }
            else {
                // set facebook stuff for email registered user
                user.facebookUid = userId
                user.facebookAccessToken  = accessToken
                user.facebookTokenExpires = expirationDate
                user = user.merge()
            }
        }
        else {
            user.refresh()
            // update the existing user
            user.facebookAccessToken  = accessToken
            user.facebookTokenExpires = expirationDate
            if (!user.avatar) {
                userService.setProfileImage(user)
            }
            user = user.merge()
        }

        securityService.existingLogin(user, request, session)

        render user as JSON
    }

    /**
     * Does a login with username/password
     * @return
     */
    def emailLogin() {
        def email = params.email
        def password = params.password

        def user = User.findByEmail(email)
        if (user && securityService.checkPassword(user, password)) {
            securityService.existingLogin(user, request, session)
            render user as JSON
        }
        else {
            render WebserviceError.badInput as JSON
        }
    }

    /**
     * Register a new user with email and password
     * @return
     */
    def emailRegister() {
        def email = params.email
        def password = params.password
        def username = params.username?.toLowerCase()
        def firstName = params.firstName
        def lastName = params.lastName
        def userAgentStr = StringUtil.limitString(request.getHeader("User-Agent"), User.defaultMaxStringLen)
        def referer = StringUtil.limitString(session.referer, User.defaultMaxStringLen)
        def enabled = true

        if (User.findByEmail(email)) {
            // already a user with that email, return an error
            render WebserviceError.alreadyExists as JSON
            return
        }
        if (User.findByUsername(username) || username.size() < 5 || !Character.isLetter(username.charAt(0))
                || !(username ==~ /^[a-zA-Z0-9.-_]+$/)) {
            render WebserviceError.duplicateUsername as JSON
            return
        }

        // make the new user object
        def user = new User(email: email, password: securityService.encodePassword(password),
            firstName: firstName, lastName: lastName, username: username,
            referer: referer, userAgent: userAgentStr, enabled: enabled)
        user.save()

        //emailService.sendEmailConfirmation(user)
        securityService.existingLogin(user, request, session)

        render user as JSON
    }

    /**
     * Changes the password of the logged in user
     * @return
     */
    def changePassword() {
        def user = User.get(session.userId)
        def oldPassword = params.oldPassword
        def newPassword = params.newPassword
        if  (securityService.checkPassword(user, oldPassword)) {
            securityService.setPassword(user, newPassword)
            render user as JSON
        }
        else {
            render WebserviceError.notAuthorized as JSON
        }
    }

    /**
     * Reset the password, takes the reset token and the new password
     * @return
     */
    def resetPassword() {
        def token = params.token
        def newPassword = params.password

        def user = User.findByPasswordResetToken(token)
        if (user) {
            securityService.setPassword(user, newPassword)
            render user as JSON
        }
        else {
            render WebserviceError.notAuthorized as JSON
        }
    }

    /**
     * Takes an email address and sends a password reset
     * @return
     */
    def forgotPassword() {
        def email = params.email
        def user = User.findByEmail(email)
        if (user) {
            //emailService.sendPasswordResetEmail(user)
            render user as JSON
        }
        else {
            render WebserviceError.badInput as JSON
        }
    }

    /**
     * Logout from the site
     * @return
     */
    def logout() {
        def user = User.get(session.userId)
        user.facebookAccessToken = null
        user.facebookTokenExpires = null
        user.save()
        session.userId = null
        flash.logout   = true
        render user as JSON
    }

}
