package helperClasses;

public class UserInfoObject {
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
}//UserInfoObject class
