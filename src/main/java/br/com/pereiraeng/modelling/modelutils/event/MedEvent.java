package br.com.pereiraeng.modelling.modelutils.event;

import java.util.Arrays;
import java.util.TreeMap;

public class MedEvent extends Event {
	private static final long serialVersionUID = -5803022599851431978L;

	protected TreeMap<Integer, float[][]> time2meds;

	protected int[][] keys;

	public MedEvent(int instl, int minute, int[][] keys, TreeMap<Integer, float[][]> past) {
		super(instl, minute);
		this.keys = keys;
		this.time2meds = past;
		assert this.time2meds.containsKey(minute);
	}

	public void put(int ci, int e, int g, float value) {
		float[][] values = time2meds.get(ci);
		if (values == null)
			time2meds.put(ci, values = new float[keys.length][]);
		int ie = -1;
		for (int i = 0; i < values.length; i++) {
			if (keys[i][g + 1] == e) {
				ie = i;
				break;
			}
		}
		if (values[ie] == null) {
			values[ie] = new float[6]; // TODO pq 6?
			Arrays.fill(values[ie], Float.NaN);
		}
		values[ie][g] = value;
	}
}
