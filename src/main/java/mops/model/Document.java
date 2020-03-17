package mops.model;

import java.io.File;
import java.io.IOException;

public interface Document {
    void save(File newFile) throws IOException;

    void setField(String fieldName, String fieldContent) throws IOException;

    void setGender(String gender) throws IOException;

    void debug();

    void addGeneralInfos() throws IOException;
}
