package de.lmu.msp.trimmdich.exercise;

public class Ringbuffer {
	private final Object[] array;

	private int head;
	private int tail;

	public Ringbuffer(int size) {
		array = new Object[size];
		head = tail = (size - 1);
	}

	public void add(Object obj) {
		head++;
		if (head >= array.length)
			head = 0;
		if (head == tail)
			tail++;
		if (tail >= array.length)
			tail = 0;
		array[head] = obj;
	}

	public Object getLatestElement() {
		return array[head];
	}

	public Object getSecondLatestElement() {
		return (head == 0) ? array[array.length - 1] : array[head - 1];
	}

	public Object getThirdLatestElement() {
		switch (head) {
		case 1:
			return array[array.length - 1];
		case 0:
			return array[array.length - 2];
		default:
			return array[head - 2];
		}
	}

	public Object getOldestElement() {
		return array[tail];
	}

	/**
	 * Größe des Ringbuffers.
	 * 
	 * @return
	 */
	public int size() {
		return array.length;
	}

	/**
	 * Anzahl der Elemente, die im Ringbuffer liegen
	 * 
	 * @return
	 */
	public int countOfElements() {
		return (head > tail && head < array.length) ? (head - tail + 1) : array.length;
	}

	public int getHead() {
		return head;
	}

	public int getTail() {
		return tail;
	}

	/**
	 * Gibt den Ringbuffer als normales Array zurück, bei dem auf [0] das älteste
	 * und auf [length-1] das neuste Element sitzt.
	 * 
	 * @return
	 */
	public synchronized Object[] toRegularArray() {
		Object[] returnArray = new Object[array.length];
		int add = tail;
		for (int i = 0; i < array.length; i++) {
			returnArray[i] = array[i + add];
			if ((i + add) == (array.length - 1)) {
				add = -(i + 1);
			}
		}
		return returnArray;
	}

	/**
	 * Gibt das Element an der gefragten Stelle zurück, wobei auf [0] immer das
	 * älteste und auf [length-1] das neuste Element sitzt.
	 * 
	 * @param pos
	 * @return
	 */
	public Object getElement(int pos) {
		return array[(pos+head+1)%array.length];
	}
	
	/**
	 * Besetzt den gesamten Ring mit dem gegebenen Objekt
	 * @param obj
	 */
	public void initWithObject(Object obj){
		for(int i=0;i<this.size();i++)
			this.add(obj);
	}
}