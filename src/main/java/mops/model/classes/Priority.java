package mops.model.classes;

import java.util.HashMap;
import java.util.Map;

public enum Priority {
    VERYHIGH("++", 1),
    HIGH("+", 2),
    NEUTRAL("O", 3),
    NEGATIVE("-", 4);

    private final String label;
    private final int value;

    private static final Map<Integer, Priority> LOOKUP = new HashMap<>();

    static {
        for (Priority p : Priority.values()) {
            LOOKUP.put(p.getValue(), p);
        }
    }

    @SuppressWarnings("checkstyle:HiddenField")
    Priority(final String label, final int value) {
        this.label = label;
        this.value = value;
    }

    /**
     * Returns Label as String.
     *
     * @return String.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns Value as int;
     *
     * @return Value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns Priority for given integer.
     *
     * @param integer Value from including 1-4
     * @return Priority.
     */
    public static Priority get(final int integer) {
        return LOOKUP.get(integer);
    }
}
