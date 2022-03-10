package fr.insee.kraftwerk.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {

    /**
     * Read a json local json file.
     *
     * @param filePath Path to a json file.
     *
     *  @return A jackson.databind.JsonNode.
     */
    public static JsonNode read(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new File(filePath));
    }

}
