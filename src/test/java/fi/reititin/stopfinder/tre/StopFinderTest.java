package fi.reititin.stopfinder.tre;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * POC of stopfinder / vm for Tampere as well. Pretty sure I won't be
 * implementing this as Tampere seems to have more comprehensive API / mobile
 * implementations already so I encourage you to use them.
 */
public class StopFinderTest extends TestCase {

	private static final Logger LOG = LoggerFactory
			.getLogger(StopFinderTest.class);

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(StopFinderTest.class);
	}

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public StopFinderTest(String testName) {
		super(testName);
	}

	public void testJsoup() throws IOException {

		String searchUrl = "http://lissu.tampere.fi/?mobile=1&search=Hae&key=koski";
		String virtualMonitorBaseUrl = "http://lissu.tampere.fi/monitor.php?stop=";
		Document doc = Jsoup.connect(searchUrl).get();
		Elements links = doc.select("a[href^=?mobile]");
		assertEquals(8, links.size());
		for (Element link : links) {
			LOG.debug(link.text());
			Attributes att = link.attributes();
			String query = att.iterator().next().toString();
			String[] params = query.split("&amp;");
			Map<String, String> map = new HashMap<String, String>();
			for (String param : params) {
				String name = param.split("=")[0];
				String value = param.split("=")[1];
				map.put(name, value);
			}

			Set<String> keys = map.keySet();
			for (String key : keys) {
				LOG.debug(key + " = " + map.get(key));
			}

			LOG.debug(virtualMonitorBaseUrl + map.get("stop"));

		}

		String vmUrl = "http://lissu.tampere.fi/monitor.php?stop=500";
		doc = Jsoup.connect(vmUrl).get();

		Element result_box = doc.select("table.table2").first();
		boolean first = true;
		boolean firstAgain = true;
		for (Element tr : result_box.select("tr")) {
			if (first) {
				first = false;
				continue;
			}
			// LOG.debug(tr.getElementsByIndexEquals(3).text() +
			// " indx");
			// LOG.debug(tr.text());
			for (Element td : tr.select("td")) {
				if (firstAgain) {
					firstAgain = false;
					continue;
				}
				if (!StringUtil.isBlank((td.text()).trim())) {
					LOG.debug("\"" + td.text() + "\"");
				}
			}
		}

	}

}
