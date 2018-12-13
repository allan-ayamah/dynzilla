package com.atena.dynzilla.core;

import java.io.IOException;
import java.io.InputStream;
// import javax.servlet.ServletContext;

import com.atena.dynzilla.ResourceLocator;
import org.apache.commons.lang.StringUtils;

public class WebAppResourceLocator{

}
/*
public class WebAppResourceLocator implements ResourceLocator {
  private ServletContext servletContext;

  public WebAppResourceLocator(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  public InputStream getInputStream(String relativePath) throws IOException {
    InputStream inStream = null;
    if (!relativePath.startsWith("/")) {
      relativePath = "/" + relativePath;
    }

    inStream = servletContext.getResourceAsStream(relativePath);
    if (inStream != null) {
      return inStream;
    }
    if (!relativePath.startsWith("/WEB-INF")) {
      inStream = servletContext.getResourceAsStream("/WEB-INF" + relativePath);
      if (inStream != null) {
        return inStream;
      }
    }

    inStream = getClass().getResourceAsStream(relativePath);
    if (inStream == null) {
      String path = StringUtils.removeStart(relativePath, "/");
      path = StringUtils.removeStart(path, "WEB-INF");
      path = StringUtils.removeStart(path, "/");
      inStream = getClass().getResourceAsStream("/" + path);
      if (inStream != null) {
        return inStream;
      }
      path = StringUtils.removeStart(path, "descr");
      path = StringUtils.removeStart(path, "/");
      inStream = getClass().getResourceAsStream("/" + path);
    }
    return inStream;
  }
}
*/