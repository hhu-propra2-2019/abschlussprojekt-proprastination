package mops.model.classes;

public enum Role {
    PROOFREADER("Korrektor"),
    TUTOR("Tutor"),
    BOTH("Korrektor und Tutor");

    private final String label;

    @SuppressWarnings("checkstyle:HiddenField")
    Role(final String label) {
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

