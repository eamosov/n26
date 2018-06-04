package ru.eamosov.n26.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Empty result with HTTP error code
 */
public enum EmptyResult {

    SUCCESS(201),
    OLD_TRANSACTION(204);

    public final int code;

    private static final Map<Integer, EmptyResult> codes = new HashMap<>();

    static {
        for (EmptyResult r : EmptyResult.values()) {
            codes.put(r.code, r);
        }
    }

    EmptyResult(int code) {
        this.code = code;
    }

    public static EmptyResult valueOf(int code) {
        return codes.get(code);
    }
}
