package helperClasses;

public class UserInfoObject {
	public String userid = null;
	public Double latitude = new Double (0);
	public Double longitude = new Double (0);
	public String interests = null;
	
	public boolean hasNull () {
		if (userid == null) return true;
		if (latitude == null) return true;
		if (longitude == null) return true;
		if (interests == null) return true;
		return false;
	}
	
	public boolean equals (UserInfoObject u) {
		if (userid != u.userid) return false;
		if (latitude != u.latitude) return false;
		if (longitude != u.longitude) return false;
		if (! interests.equals (u.interests)) return false;
		return true;
	}
}
