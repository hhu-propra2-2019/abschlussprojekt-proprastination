package mops.model.classes.webclasses;

import lombok.Data;

@Data
public class DownloadProgress {
    private long progress = 0;
    private long size = 0;
    private boolean finished = false;

    /**
     * Increments progress by 1.
     */
    public void addProgress() {
        progress++;
    }

    /**
     * Adds maaxsize by parameter.
     *
     * @param size entry size
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public void addSize(final long size) {
        this.size += size;
    }

    /**
     * Flushes data.
     */
    public void zero() {
        progress = 0;
        size = 0;
        finished = false;
    }
}
