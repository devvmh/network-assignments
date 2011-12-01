package helperClasses;

import java.math.RoundingMode;
import java.util.List;
import java.util.Collections;

import android.location.Location;

public class UserInfoObject implements Comparable<UserInfoObject> {
	public String intIP = null;
	public String extIP = null;
	public Double latitude = new Double (0);
	public Double longitude = new Double (0);
	public String interests = null;
	
	public boolean hasNull () {
		if (intIP == null) return true;
		if (extIP == null) return true;
		if (latitude == null) return true;
		if (longitude == null) return true;
		if (interests == null) return true;
		return false;
	}
	
	public boolean equals (UserInfoObject u) {
		return (intIP.equals(u.intIP) &&
				extIP.equals(u.extIP) &&
				latitude.equals(u.latitude) &&
				longitude.equals (u.longitude) &&
				interests.equals (u.interests));
	}
	
	public float getDistance (UserInfoObject u) {
		float [] dist = {0};
		Location.distanceBetween(this.latitude, this.longitude, u.latitude, u.longitude, dist);
		return round(dist [0], 2);
	}

	public static void sort (List<UserInfoObject> lst) {
		Collections.sort(lst);
	}


	//for sorting
	@Override
	public int compareTo(UserInfoObject u) {
		UserInfoObject user = GlobalVariables.self;
		float [] distArray = {0};
		Location.distanceBetween(user.latitude, user.longitude, this.latitude, this.longitude, distArray);
		float myDistanceToUser = distArray [0];
		
		Location.distanceBetween (user.latitude, user.longitude, u.latitude, u.longitude, distArray);
		float theirDistanceToUser = distArray [0];
		
		return (int) (myDistanceToUser - theirDistanceToUser);
	}
	
	
	private float round(float Rval, int Rpl) {
		float p = (float)Math.pow(10,Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);
		return (float)tmp/p;
	}
}//UserInfoObject class
