package org.deguet.gutils.geohash;

import org.deguet.gutils.bit.BitLine;
import org.deguet.gutils.nuplets.Duo;

import java.util.ArrayList;
import java.util.List;

public final class WeekSchedule {

	public enum Days {Mon,Tue,Wed,Thu,Fri,Sat,Sun};
	//public enum HalfHour {First, Last}

	private final BitLine line;

	/**
	 * Creates an empty schedule
	 */
	public WeekSchedule(){
		BitLine tmp = new BitLine(24*7*2);
		line = tmp;
	}

	public static WeekSchedule classical(){
		WeekSchedule result = new WeekSchedule();
		for (Days day : Days.values()){
			for (int hour = 11; hour <23 ; hour ++){
				result = result.setAt(true, day, hour*2).setAt(true, day, hour*2+1);
			}
		}
		return result;
	}

	private WeekSchedule(final BitLine line){
		this.line = line;
	}

	public WeekSchedule setAt(boolean open, Days day, int halfhour){
		BitLine nl = this.line;
		nl = nl.set(day.ordinal()*(48)+halfhour, open);
		return new WeekSchedule(nl);
	}

	private List<Duo<String,String>> intervalsForDay(Days day){
		List<Duo<String,String>> res = new ArrayList<Duo<String,String>>();
		boolean interval=false;
		int interstart = 0;
		for (int ind = day.ordinal()*48 ; ind < day.ordinal()*(48)+48 ; ind++){
			if (line.isOn(ind)){
				if (!interval){
					interval = true;
					interstart = ind;
				}
			}
			else{
				if (interval){
					res.add(
							Duo.d(
									intToHour(interstart-day.ordinal()*48),
									intToHour(ind-day.ordinal()*48)
							)
					);
					interval = false;
				}
			}
		}
		return res;
	}

	private String intToHour(int hour){
		return ""+hour/2+":"+(hour%2==1?"30":"00");
	}

	@Override
	public String toString(){
		String res = "";
		for (Days day : Days.values()){
			res += "\n"+day.toString()+this.intervalsForDay(day);
		}
		return res;
	}

	public boolean isOpen(Days day, int halfhour) {
		return line.isOn(day.ordinal()*(48)+halfhour);
	}

	public BitLine line() {return this.line;}

}
