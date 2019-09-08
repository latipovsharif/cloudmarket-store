package com.vvmarkets.errors;

import java.io.IOException;

public class NotAuthorized extends IOException {
    public NotAuthorized(String message) {
        super(message);
    }
}
