package mops.model.classes;

public enum Priority {
    SehrHoch("++"),
    Hoch("+"),
    Neutral("O"),
    Negative("-");

    private final String label;

    @SuppressWarnings("checkstyle:HiddenField")
    Priority(final String label) {
        this.label = label;
    }

    /**
     * Returns Label as String.
     *
     * @return String.
     */
    public String getLabel() {
        return label;
    }
}
