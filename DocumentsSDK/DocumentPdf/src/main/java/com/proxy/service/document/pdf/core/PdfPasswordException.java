package com.proxy.service.document.pdf.core;

import java.io.IOException;

public class PdfPasswordException extends IOException {
    public PdfPasswordException() {
        super();
    }

    public PdfPasswordException(String detailMessage) {
        super(detailMessage);
    }
}
