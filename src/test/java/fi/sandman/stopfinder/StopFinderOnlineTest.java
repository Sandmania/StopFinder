package fi.sandman.stopfinder;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fi.sandman.stopfinder.Stop;
import fi.sandman.stopfinder.StopFinder;

/**
 * <p>
 * Test stop finder in online mode = makes real requests to <a
 * href="http://info.jyvaskylanliikenne.fi"
 * />http://info.jyvaskylanliikenne.fi</a>
 * </p>
 * 
 * These tests are likely to fail if something changes in the website.
 * 
 * @author Jouni Latvatalo <jouni.latvatalo@gmail.com>
 */
public class StopFinderOnlineTest {

	@Test
	public void testSearchStops() throws IOException {
		List<Stop> stops = StopFinder.searchStops("kirjasto");
		// At the moment five stops should be found with the keyword "kirjasto"
		Assert.assertEquals(5, stops.size());
	}
}
