package fi.sandman.stopfinder;

/**
 * A class that represents a single line on the {@link VirtualMonitor} view.
 * Contains information of bus line number, route, original departure time and
 * possible new departure time.
 * 
 * @author Jouni Latvatalo <jouni.latvatalo@gmail.com>
 * 
 */
public class TimeRow {

	private String lineNumber;
	private String newTime;
	private String originalTime;
	private String route;

	public String getLineNumber() {
		return lineNumber;
	}

	public String getNewTime() {
		return newTime;
	}

	public String getOriginalTime() {
		return originalTime;
	}

	public String getRoute() {
		return route;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setNewTime(String newTime) {
		this.newTime = newTime;
	}

	public void setOriginalTime(String originalTime) {
		this.originalTime = originalTime;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public void setTimeInformations(String text) {
		if (text.length() > 5) {
			this.setNewTime(text.substring(6, text.length()));
			this.setOriginalTime(text.substring(0, 5));
		} else {
			this.setOriginalTime(text);
		}

	}

}
