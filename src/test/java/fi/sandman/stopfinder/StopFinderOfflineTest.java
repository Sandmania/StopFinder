package fi.sandman.stopfinder;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.easymock.EasyMock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fi.sandman.stopfinder.Stop;
import fi.sandman.stopfinder.StopFinder;
import fi.sandman.stopfinder.VirtualMonitor;

/**
 * <p>
 * Test stop finder in offline mode = I have copied responses from <a
 * href="http://info.jyvaskylanliikenne.fi"
 * />http://info.jyvaskylanliikenne.fi</a> and saved them as static html files.
 * EasyMock is used to mock the responses.
 * </p>
 * 
 * These test the actual functionality of the scraping.
 * 
 * @author Jouni Latvatalo <jouni.latvatalo@gmail.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ StopFinder.class })
public class StopFinderOfflineTest {

	@Test
	public void testGetVirtualMonitorInfo() throws IOException {
		Stop keskusta7 = new Stop();
		keskusta7.setName("Jyväskylä Keskusta 7");
		keskusta7.setCode("Keskusta%2320945.10347");

		PowerMock.mockStaticPartial(StopFinder.class, "getDocument");

		Document doc = Jsoup
				.parse(ClassLoader
						.getSystemResourceAsStream("keskusta7VirtualMonitorStatic.html"),
						"UTF-8", "http://localhost/");

		String searchTerm = keskusta7.getCode() + "|"
				+ URLEncoder.encode(keskusta7.getName(), "UTF-8");

		EasyMock.expect(
				StopFinder.getDocument(StopFinder.VM_BASE_URL + searchTerm))
				.andReturn(doc);

		PowerMock.replay(StopFinder.class);

		VirtualMonitor vm = StopFinder.getVirtualMonitorInfo(keskusta7);
		// the static file contains 22 time rows
		Assert.assertEquals(22, vm.getTimeRows().size());

		Assert.assertEquals("17.28", vm.getTimeRows().get(0).getOriginalTime());
		Assert.assertEquals("17.34", vm.getTimeRows().get(0).getNewTime());
		Assert.assertEquals("Kauppatori - Mustalampi", vm.getTimeRows().get(0)
				.getRoute());

		Assert.assertEquals("19.20", vm.getTimeRows().get(21).getOriginalTime());
		Assert.assertNull(vm.getTimeRows().get(21).getNewTime());
		Assert.assertEquals("Keskusta - Kortemäki", vm.getTimeRows().get(21)
				.getRoute());
	}

	@Test
	public void testSearchStops() throws IOException {

		PowerMock.mockStaticPartial(StopFinder.class, "getDocument");

		Document doc = Jsoup.parse(ClassLoader
				.getSystemResourceAsStream("stopSearchKeskustaStatic.html"),
				"UTF-8", "http://localhost/");

		String searchTerm = "keskusta";
		EasyMock.expect(
				StopFinder.getDocument(StopFinder.STOP_SEARCH_BASE_URL
						+ URLEncoder.encode(searchTerm, "UTF-8"))).andReturn(
				doc);

		PowerMock.replay(StopFinder.class);
		List<Stop> stops = StopFinder.searchStops(searchTerm);

		// At the moment one hundred stops should be found with the keyword
		// "keskusta"
		Assert.assertEquals(100, stops.size());
	}
}
