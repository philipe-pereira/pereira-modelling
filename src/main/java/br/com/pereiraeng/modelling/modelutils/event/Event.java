package br.com.pereiraeng.modelling.modelutils.event;

import java.io.Serializable;
import java.util.Calendar;

import br.com.pereiraeng.core.TimeUtils;

public abstract class Event implements Comparable<Event>, Serializable {
	private static final long serialVersionUID = -8977817515619264242L;

	protected final int space;

	protected final int time;

	public Event(int space, int time) {
		this.space = space;
		this.time = time;
	}

	public Event(long id) {
		this.space = (int) id;
		this.time = (int) (id >> 32);
	}

	public int getSpace() {
		return space;
	}

	public Calendar getTime() {
		return TimeUtils.toCalendar(getTimeInt());
	}

	public int getTimeInt() {
		return this.time;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Event) {
			Event e = (Event) anObject;
			return e.space == this.space && e.time == this.time;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(space + time).hashCode();
	}

	@Override
	public int compareTo(Event e) {
		int out = e.time - this.time;
		if (out == 0) {
			out = e.space - this.space;
			if (out == 0)
				return e.getClass().getName().compareTo(this.getClass().getName());
			else
				return out;
		} else
			return out;
	}

	public long getId() {
		return (((long) this.time) << 32) + this.space;
	}
}
