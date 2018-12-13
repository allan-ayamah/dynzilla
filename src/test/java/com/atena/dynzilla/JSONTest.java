package com.atena.dynzilla;

import java.io.*;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class JSONTest {
    private String descrFolder;

    @Before
    public void setUp() {
        descrFolder = "C:/Users/Au19/Workspace/projects/dynzilla/src/test/resources/WEB-INF/descr/";
    }

    @Test
    public void testReadDescr() throws DynException {
        String id = "_dyn_model";
        String modelId = "mdl1";
        JsonObject descr = readDescriptor(modelId +"/"+id);
        System.out.println(descr.get("manager"));
        System.out.println(descr);
    }

    private JsonObject readDescriptor(String id) throws DynException{
        String relativePath = descrFolder + id + ".json";
        InputStream in =  null;
        try {
            in = getResourceStream(relativePath);
        } catch (DynException e2) {
            logError("", e2);
            IOUtils.closeQuietly(in);
            throw e2;
        }

        // read descripto
        JsonObject descr =  null;
        try{
            String content = IOUtils.toString(in , Charset.defaultCharset());
            Gson gson = new Gson();
            descr = gson.fromJson(content, JsonObject.class);
            return descr;
        } catch (IOException e) {
            logError("Could not read descriptor" + id, e);
            throw new DynException("Could not read descriptor" + id, e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }



    public InputStream getResourceStream(String relativePath) throws DynException {
        InputStream in = null;
        try {
            in = new FileInputStream(relativePath);
        } catch (FileNotFoundException e) {
            throw new DynException("Could not find resource at "+ relativePath , e);
        }
        return in;
    }


    private void logError(String msg, Throwable e) {
        System.out.println(msg);
        e.printStackTrace();
    }
}
