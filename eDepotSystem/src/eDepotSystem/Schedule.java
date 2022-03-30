package eDepotSystem;

import java.util.Date;
import java.util.ArrayList;

public interface Schedule {
		public Boolean isAvailable(Date startDate, Date endDate);
		public void addSchedule(WorkSchedule schedule);
		public ArrayList<WorkSchedule> getSchedule();
}

