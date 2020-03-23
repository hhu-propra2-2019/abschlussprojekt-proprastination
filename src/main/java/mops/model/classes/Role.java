package mops.model.classes;

public enum Role {
    KORREKTOR("Korrektor"),
    TUTOR("Tutor"),
    NONE("Ist mir egal");

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

