package cz.mzk.fofola.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AccessType {

    PUBLIC("public"),
    PRIVATE("private");

    private final String name;

    AccessType(final String name) {
        this.name = name;
    }

    public static AccessType of(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("name of AccessType can't be null!");
        }

        return Arrays.stream(AccessType.values())
                .filter(value -> value.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown AccessType: " + name));
    }
}
