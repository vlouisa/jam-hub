package dev.louisa.jam.hub.infrastructure.security.util;

import static org.springframework.util.StringUtils.replace;

public class RSAKeyReaderParameters {
    private static final String TEMPLATE_BUNDLE_KID_FILE = "keys/{}.kid";
    private static final String TEMPLATE_BUNDLE_PUBLIC_KEY_FILE = "keys/{}.key.pub";
    private static final String TEMPLATE_BUNDLE_PRIVATE_KEY_FILE = "keys/{}.key";

    public static String keyIdFileLocation(String bundleName) {
        return replace(TEMPLATE_BUNDLE_KID_FILE, "{}", bundleName);
    }

    public static String publicKeyFileLocation(String bundleName) {
        return replace(TEMPLATE_BUNDLE_PUBLIC_KEY_FILE, "{}", bundleName);
    }

    public static String privateKeyFileLocation(String bundleName) {
        return replace(TEMPLATE_BUNDLE_PRIVATE_KEY_FILE, "{}", bundleName);
    }
}
