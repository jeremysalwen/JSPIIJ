package java.awt;

import java.awt.event.ActionEvent;

/*
 * I have to make this in the java.awt package to be able to implement the interface EventFilter
 */
public class LardStrainer implements EventFilter {
	appletProxy proxy;

	public LardStrainer(appletProxy filterforme) {
		this.proxy = filterforme;
	}

	public FilterAction acceptEvent(AWTEvent ev) {
		if (ev.getSource() == proxy.canvas && !(ev instanceof ActionEvent)) {
			ev.setSource(proxy);
			return FilterAction.ACCEPT;
		} else {
			return FilterAction.ACCEPT;
		}
	}

}
