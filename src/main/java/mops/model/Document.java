package mops.model;

import java.io.File;
import java.io.IOException;

public interface Document {
    /**
     * Interface to Save Document.
     *
     * @param newFile FIle to save.
     * @throws IOException Exception.
     */
    void save(File newFile) throws IOException;

    /**
     * Sets field of Document.
     *
     * @param fieldName    Name of field.
     * @param fieldContent Fieldcontent.
     * @throws IOException Could not write/read.
     */
    void setField(String fieldName, String fieldContent) throws IOException;

    /**
     * Sets Genderfield in Document. This function is due to bug in PDF.
     *
     * @param gender gender {"männlich","weiblich"}
     * @throws IOException IOException.
     */
    void setGender(String gender) throws IOException;

    /**
     * Print debug Data // Prints all PDF field names to System::out
     */
    void debug();

    /**
     * Adding gernal Infos for all Applications
     *
     * @throws IOException IOException.
     */
    void addGeneralInfos() throws IOException;
}
