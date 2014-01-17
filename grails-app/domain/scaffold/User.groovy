// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import java.util.Date


/**
 * This is the main user class
 * @author jwarner
 *
 */
class User {
    static def defaultMaxStringLen = 255

    // traditional login stuff
    String email
    String password
    String passwordResetToken
    String emailConfirmToken
    Date   emailConfirmed

    // personal info
    String name // derived field with first name and last name
    String firstName
    String lastName

    // login data
    String referer
    String userAgent
    String mobileDeviceId          // wurfl id from spring mobile plugin

    // facebook stuff
    Long facebookUid
    String facebookAccessToken
    Date facebookTokenExpires
    String username

    // access control stuff
    Boolean enabled = false        // for now, accounts start disabled
    boolean accountLocked = false  // they can no longer log in
    Boolean hidden = false         // boolean to indicate that we hide them in search results
    Boolean hideFromSearch         // derived property to use the above to tell us if we should hide the user from search results

    Date lastUpdated
    Date created = new Date()

    Collection roles
    Collection userTokens
    static hasMany = [roles: Role, userTokens: UserToken]

    static mapping = {
        name formula: 'CONCAT(first_name, " ", last_name)'
        hideFromSearch formula: 'IF(enabled = 1 and signup_completed is not null and NOT(account_locked = 1) and NOT(hidden = 1), 0, 1)'
        autoTimestamp true
        datasources(['DEFAULT', 'readReplica'])
        //cache true
    }

    static constraints = {
        email unique: true
        username nullable: true, unique: true
        password nullable: true
        firstName nullable: true
        lastName nullable: true
        passwordResetToken unique: true, nullable: true
        emailConfirmToken unique: true, nullable: true
        emailConfirmed nullable: true
        referer nullable: true
        userAgent nullable: true
        mobileDeviceId nullable: true
        facebookUid nullable: true, unique: true
        facebookAccessToken nullable: true
        facebookTokenExpires nullable: true
        enabled nullable: true
        hidden nullable: true
    }

}
