package com.colossus.notify.mailgun.vo;

import lombok.Data;

import java.io.File;

@Data
public class Attachment {

    public Attachment() {
    }

    public Attachment(File filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    private File filePath;
    private String fileName;
}
