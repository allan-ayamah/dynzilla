package com.atena.dynzilla.core;

import com.atena.dynzilla.AbstractDYNServiceTest;
import com.atena.dynzilla.DYNException;
import com.atena.dynzilla.DYNLinkService;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class LinkServiceTest extends AbstractDYNServiceTest {

  @Test
  public void testPropagate() throws DYNException {
    String lnlJsonDescr =
        "{\n"
            + "  \"service\": \"com.atena.dynzilla.core.LinkService\",\n"
            + "  \"name\": \"Calucute a plus b\",\n"
            + "  \"sourceId\": \"form\",\n"
            + "  \"targetId\": \"sumc1\",\n"
            + "  \"propagations\": [\n"
            + "    {\n"
            + "      \"sourceParam\": \"form.aOutput\",\n"
            + "      \"targetParam\": \"sumc1.a\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"sourceParam\": \"form.bOutput\",\n"
            + "      \"targetParam\": \"sumc1.b\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"constantSourceValue\": \"99\",\n"
            + "      \"targetParam\": \"sumc1.c\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"constantSourceValue\": \"99|99\",\n"
            + "      \"targetParam\": \"sumc1.d\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    String id = "ln1";
    Map descr = DescriptorHelper.fromJson(lnlJsonDescr);
    DYNLinkService linkService = new LinkService(id, null, descr);

    // Result of the source component after execution
    Map formResultBean =
        new HashMap() {
          {
            put("form.aOutput", 1);
            put("form.bOutput", 2);
          }
        };
    Map targetContext = new HashMap();
    linkService.propagateFromResult(formResultBean, targetContext, null);

    assertEquals(formResultBean.get("form.aOutput"), targetContext.get("sumc1.a"));
    assertEquals(formResultBean.get("form.bOutput"), targetContext.get("sumc1.b"));

    assertEquals("99", targetContext.get("sumc1.c"));
    assertEquals("99|99", targetContext.get("sumc1.d"));
  }
}
