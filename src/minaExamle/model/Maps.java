package minaExamle.model;
/**
 * 
 * @author hqh
 * @createtime 2015-6-5
 * @注释：全图监�?
 */
public class Maps {
	private String monitorid;//id
	private String lng;
	private String lat;
	private String carid;//汽车id
	private String beidouid;//设备id
	private String nowspace;//位置
	private String nowdate;//定位时间
	private String state;//运行，静止，离线
	private String departmentid;//部门id
	private String gpsstate;//运行速度/或显示设备关�?
	private String angle;//角度
	private String inssj;
	private int isspeed;
	private String originallng;//
	private String originallat;//
	
	private String chaju;//差距
	
	
	public String getChaju() {
		return chaju;
	}

	public void setChaju(String chaju) {
		this.chaju = chaju;
	}

	public String getOriginallng() {
		return originallng;
	}

	public void setOriginallng(String originallng) {
		this.originallng = originallng;
	}

	public String getOriginallat() {
		return originallat;
	}

	public void setOriginallat(String originallat) {
		this.originallat = originallat;
	}

	public String getInssj() {
		return inssj;
	}

	public void setInssj(String inssj) {
		this.inssj = inssj;
	}

	public int getIsspeed() {
		return isspeed;
	}

	public void setIsspeed(int isspeed) {
		this.isspeed = isspeed;
	}
	
	public String getAngle() {
		return angle;
	}
	
	public void setAngle(String angle) {
		this.angle = angle;
	}

	//后加�?-做定时器 修改车辆状�?使用的属�?
	private int timesmin;
	
	public int getTimesmin() {
		return timesmin;
	}
	
	public void setTimesmin(int timesmin) {
		this.timesmin = timesmin;
	}
	//后加�?-回放停留点信�?
	/*private String sitestarttime;
	private String siteendtime;
	private String sitetimelong;
	private String sitelng;
	private String sitelat;
	

	public String getSitestarttime() {
		return sitestarttime;
	}

	public void setSitestarttime(String sitestarttime) {
		this.sitestarttime = sitestarttime;
	}

	public String getSiteendtime() {
		return siteendtime;
	}

	public void setSiteendtime(String siteendtime) {
		this.siteendtime = siteendtime;
	}

	public String getSitetimelong() {
		return sitetimelong;
	}

	public void setSitetimelong(String sitetimelong) {
		this.sitetimelong = sitetimelong;
	}

	public String getSitelng() {
		return sitelng;
	}

	public void setSitelng(String sitelng) {
		this.sitelng = sitelng;
	}

	public String getSitelat() {
		return sitelat;
	}

	public void setSitelat(String sitelat) {
		this.sitelat = sitelat;
	}*/

	//临时属�?
	private String drivename;//司机名字
	private String drivenumber;//司机电话
	private String carno;//车牌�?
	private String deptname;//部门名字
	private String beidouno;//设备�?
	
	public String getBeidouno() {
		return beidouno;
	}

	public void setBeidouno(String beidouno) {
		this.beidouno = beidouno;
	}

	public String getDrivename() {
		return drivename;
	}

	public void setDrivename(String drivename) {
		this.drivename = drivename;
	}

	public String getDrivenumber() {
		return drivenumber;
	}

	public void setDrivenumber(String drivenumber) {
		this.drivenumber = drivenumber;
	}

	public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getMonitorid() {
		return monitorid;
	}
	public void setMonitorid(String monitorid) {
		this.monitorid = monitorid;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getCarid() {
		return carid;
	}
	public void setCarid(String carid) {
		this.carid = carid;
	}
	public String getBeidouid() {
		return beidouid;
	}
	public void setBeidouid(String beidouid) {
		this.beidouid = beidouid;
	}
	public String getNowspace() {
		return nowspace;
	}
	public void setNowspace(String nowspace) {
		this.nowspace = nowspace;
	}
	public String getNowdate() {
		return nowdate;
	}
	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}
	public String getGpsstate() {
		return gpsstate;
	}
	public void setGpsstate(String gpsstate) {
		this.gpsstate = gpsstate;
	}
}
