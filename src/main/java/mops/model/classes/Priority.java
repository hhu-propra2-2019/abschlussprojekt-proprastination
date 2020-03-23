package mops.model.classes;

public enum Priority {
    SehrHoch("++", 1),
    Hoch("+", 2),
    Neutral("O", 3),
    Negative("-", 4);

    private final String label;
    private final int value;

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
}
