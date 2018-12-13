package com.atena.dynzilla;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceLocator {

    InputStream getInputStream(String relativePath) throws IOException;
}
