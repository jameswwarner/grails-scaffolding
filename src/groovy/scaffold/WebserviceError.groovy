// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

/**
 * Enum of errors encountered that can be rendered from Web Services
 * @author jwarner
 *
 */
enum WebserviceError {
    resourceNotFound  (100, "Resource does not exist"),
    badInput          (101, "Invalid input"),
    notAuthorized     (102, "Not authorized"),
    invalidDomain     (103, "Save not valid. Check uniqueness."),
    notLoggedIn       (104, "User is not logged in."),
    alreadyExists     (105, "Object already exists."),
    duplicateUsername (106, "Username already in use."),
    internalError     (900, "Internal error. Please report to support."),
    unknown           (999, "Unknown error encountered")

    final int code
    final String msg

    private WebserviceError(int code, String msg) {
       this.code = code
       this.msg = msg
    }

    int code() { return code }
    String msg() { return msg }

    static WebserviceError fromCode(int code) {
        for (WebserviceError err : values())
           if (err.code.equals(code))
               return err
        return WebserviceError.unknown
    }

}
