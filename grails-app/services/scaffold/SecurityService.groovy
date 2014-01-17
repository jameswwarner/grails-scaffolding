// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

/**
 * For all things user security related - checking passwords, changing passwords, etc.
 * @author jwarner
 *
 */
class SecurityService {
    def bcryptService
    def tokenService

    /**
     * Returns true if the password matches, false otherwise.
     * @param user
     * @param password
     * @return
     */
    def checkPassword(def user, def password) {
        return bcryptService.checkPassword(password, user.password)
    }

    /**
     * Sets the password of the given user to the given new password.
     * NOTE: you have to either check the password OR the reset token before doing this.
     * @param user
     * @param password
     * @return
     */
    def setPassword(def user, def password) {
        user.passwordResetToken = null
        user.password = bcryptService.hashPassword(password)
        user.save()
    }

    /**
     * Encode the password with bcrypt
     * @param password
     * @return
     */
    def encodePassword(def password) {
        return bcryptService.hashPassword(password)
    }

    /**
     * Tasks that need to happen when an existing user logs in
     * @param user
     * @param request
     * @param session
     * @return
     */
    def existingLogin(def user, def request, def session) {
        tokenService.updateUserToken(user, request.token)
        tokenService.recordSession(user, request, session)

        session.userId = user.id
        session.requireLogin = false
    }
}
