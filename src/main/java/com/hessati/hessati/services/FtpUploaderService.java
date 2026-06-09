package com.hessati.hessati.services;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FtpUploaderService {

    private String server;
    private int port;
    private String user;
    private String pass;

    public FtpUploaderService(String server, int port, String user, String pass) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public String uploadFile(MultipartFile file, String remoteDir) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // Generate unique file name
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String generatedName = UUID.randomUUID().toString() + extension;

            String remoteFileName = (remoteDir.isEmpty() ? "" : remoteDir + "/") + generatedName;

            try (InputStream inputStream = file.getInputStream()) {
                boolean done = ftpClient.storeFile(remoteFileName, inputStream);
                if (done) {
                    return remoteFileName;
                } else {
                    throw new IOException("Upload failed: " + ftpClient.getReplyString());
                }
            }
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    public boolean deleteFile(String remoteDir, String fileName) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String remoteFilePath = (remoteDir.isEmpty() ? "" : remoteDir + "/") + fileName;

            boolean deleted = ftpClient.deleteFile(remoteFilePath);
            if (!deleted) {
                throw new IOException("Could not delete file: " + remoteFilePath + " (check path or permissions)");
            }
            return true;
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

}
