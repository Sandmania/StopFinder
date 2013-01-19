package fi.sandman.stopfinder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * An util class for searching bus stops and virtual monitor (timetable)
 * information from
 * </p>
 * 
 * http://info.jyvaskylanliikenne.fi/index.php?ua=search&v=vm_stops
 * 
 * @author Jouni Latvatalo <jouni.latvatalo@gmail.com>
 * 
 */
public class StopFinder {

	private static final Logger LOG = LoggerFactory.getLogger(StopFinder.class);
	public static final String STOP_SEARCH_BASE_URL = "http://info.jyvaskylanliikenne.fi/index.php?submit=Hae&ua=search&v=vm_stops&key=";
	private static final int TIMEOUT_MS = 10000;

	public static final String VM_BASE_URL = "http://info.jyvaskylanliikenne.fi/index.php?ua=monitor&v=monitor&lcn=";

	/**
	 * Parses stop name and code from found stop links. Creates stops from this
	 * information and returns a list of them.
	 * 
	 * @param stopLinks
	 * @return
	 */
	private static List<Stop> generateStops(Elements stopLinks) {
		List<Stop> stops = new ArrayList<Stop>();
		Stop stop;
		for (Element link : stopLinks) {
			stop = new Stop();
			Attributes att = link.attributes();
			String query = att.iterator().next().toString();
			String[] params = query.split("&amp;");
			Map<String, String> map = new HashMap<String, String>();
			for (String param : params) {
				String name = param.split("=")[0];
				String value = param.split("=")[1];
				map.put(name, value);
			}
			try {
				stop.setName(URLDecoder.decode(map.get("name_list"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				LOG.error("generateStops", e);
			}
			stop.setCode(map.get("stop_list"));
			stops.add(stop);
		}
		return stops;
	}

	/**
	 * Creates virtual monitor information from the given html element
	 * 
	 * @param resultBox
	 * @return
	 */
	private static VirtualMonitor generateVirtualMonitor(Element resultBox) {
		VirtualMonitor vm = new VirtualMonitor();
		TimeRow timeRow;
		// the first row always seems to be empty
		boolean first = true;
		for (Element tr : resultBox.select("tr")) {
			if (first) {
				first = false;
				continue;
			}
			// Seems that we don't have all the data we were looking for, lets
			// continue the loop
			if (tr.select("td").size() < 4) {
				continue;
			}
			timeRow = new TimeRow();
			timeRow.setTimeInformations(tr.select("td").get(1).text());
			timeRow.setLineNumber(tr.select("td").get(2).text());
			// replace non-breaking space characters with a whitespace (.trim()
			// doesn't recognize nbsp chars)
			String route = tr.select("td").get(3).text()
					.replace(String.valueOf((char) 160), " ");
			timeRow.setRoute(route.trim());
			vm.addTimeRow(timeRow);
		}
		return vm;
	}

	/**
	 * <p>
	 * Gets a {@link Document} from the given url.
	 * </p>
	 * 
	 * <p>
	 * The purpose of this method is to make offline testing easier.
	 * </p>
	 * 
	 * @param url
	 * @return {@link Document}
	 * @throws IOException
	 */
	public static Document getDocument(String url) throws IOException {
		return Jsoup.connect(url).timeout(TIMEOUT_MS).get();
	}

	/**
	 * Constructs a {@link VirtualMonitor} for the given {@link Stop}. The
	 * monitor contains information of busses departing from that stop in the
	 * near future.
	 * 
	 * @param stop
	 * @return
	 * @throws SocketTimeoutException
	 */
	public static VirtualMonitor getVirtualMonitorInfo(Stop stop)
			throws SocketTimeoutException {
		VirtualMonitor vm = null;
		try {
			String vmSearchTerm = stop.getCode() + "|"
					+ URLEncoder.encode(stop.getName(), "UTF-8");
			String url = VM_BASE_URL + vmSearchTerm;
			Document doc = getDocument(url);
			Element resultBox = doc.select("div.result_box").first();
			vm = generateVirtualMonitor(resultBox);
		} catch (UnsupportedEncodingException e) {
			LOG.error("getVirtualMonitorInfo", e);
		} catch (IOException e) {
			LOG.error("getVirtualMonitorInfo", e);
		}
		return vm;
	}

	/**
	 * Returns bus stops found with the given search term
	 * 
	 * @param searchTerm
	 * @return
	 */
	public static List<Stop> searchStops(String searchTerm) {
		List<Stop> foundStops = null;
		try {
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			Document doc = getDocument(STOP_SEARCH_BASE_URL + encodedSearchTerm);
			Elements stopLinks = doc
					.select("a[href$=&key=" + encodedSearchTerm);
			foundStops = generateStops(stopLinks);
		} catch (UnsupportedEncodingException e) {
			LOG.error("searchStops", e);
		} catch (IOException e) {
			LOG.error("searchStops", e);
		}
		return foundStops;
	}

	/**
	 * Not meant to be instantiated
	 */
	private StopFinder() {
	}

}
