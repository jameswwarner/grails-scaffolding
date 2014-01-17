// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTP


/**
 * Service for communication with FTP.
 * @author jwarner
 */
class FtpService {

    boolean transactional = true

    String server = 'localhost'
    String username = 'trungsi'
    String passwd = 'trungsi'

    String remoteBaseDir = '_photos'

    def configure(def server, def username, def password, def remoteBaseDir) {
        this.server = server
        this.username = username
        this.passwd = password
        this.remoteBaseDir = remoteBaseDir
    }

    def save(inputStream, fileName) {
        connect {ftp -> ftp.storeFile "${remoteBaseDir}/${fileName}", inputStream}
    }

    def load(fileName) {
        connect ({ftp ->
            ftp.retrieveFileStream "${remoteBaseDir}/${fileName}"
        }, false)
    }

    def delete(fileName) {
        connect {ftp -> ftp.deleteFile "${remoteBaseDir}/${fileName}"}
    }

    private def connect(Closure c, boolean discOnFinish = true) {
        def ftp = new FTPClient()
        ftp.connect server
        ftp.login username, passwd
        ftp.fileType = FTP.IMAGE_FILE_TYPE

        try {
            return c?.call(ftp)
        } finally {
            if (discOnFinish)
                ftp.logout()
            ftp.disconnect()
        }
    }
}
