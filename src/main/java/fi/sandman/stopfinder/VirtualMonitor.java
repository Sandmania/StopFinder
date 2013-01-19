package fi.sandman.stopfinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information of departing busses ({@link TimeRow)} from a certain
 * {@link Stop}
 * 
 * @author Jouni Latvatalo <jouni.latvatalo@gmail.com>
 * 
 */
public class VirtualMonitor {

	private Stop stop;
	private List<TimeRow> timeRows;

	public void addTimeRow(TimeRow timeRow) {
		if (this.timeRows == null) {
			this.timeRows = new ArrayList<TimeRow>();
		}
		this.timeRows.add(timeRow);
	}

	public Stop getStop() {
		return stop;
	}

	public List<TimeRow> getTimeRows() {
		return timeRows;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public void setTimeRows(List<TimeRow> timeRows) {
		this.timeRows = timeRows;
	}

}
