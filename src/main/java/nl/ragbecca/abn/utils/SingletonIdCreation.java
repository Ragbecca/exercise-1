package nl.ragbecca.abn.utils;

import java.security.SecureRandom;
import java.time.Instant;

public class SingletonIdCreation {

    private static SingletonIdCreation singleInstance = null;

    public static final SecureRandom random = new SecureRandom();

    public static synchronized SingletonIdCreation getInstance() {
        if (singleInstance == null) {
            singleInstance = new SingletonIdCreation();
        }
        return singleInstance;
    }

    public String createId() {
        String now = Instant.now().toString();
        String randomSuffix = String.valueOf(random.nextInt());
        return now + "_" + randomSuffix;
    }
}
