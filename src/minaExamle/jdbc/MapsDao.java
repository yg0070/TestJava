package minaExamle.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import minaExamle.model.Maps;

public class MapsDao {
	public static int saveMaps(Maps map){
		DBHelper db=new DBHelper();
		String sql="insert into jy_monitor(monitorid,lng,lat,beidouid,nowdate,state,gpsstate,angle,inssj,nowspace) VALUES(?,?,?,?,?,?,?,?,?,?)";
		String[]can=new String[]{map.getMonitorid(),map.getLng(),map.getLat(),map.getBeidouid(),map.getNowdate(),map.getState(),map.getGpsstate(),map.getAngle(),map.getInssj(),map.getNowspace()};
		System.out.println(map.getMonitorid()+"\t"+map.getLng()+"\t"+map.getLat()+"\t"+map.getBeidouid()+"\t"+map.getNowdate()+"\t"+map.getState()+"\t"+map.getGpsstate()+"\t"+map.getAngle()+"\t"+map.getInssj()+"\t"+map.getNowspace());
		return db.save(sql, can);
	}
	public static int getCar(String beidouId){
		DBHelper db=new DBHelper();
		String sql="select count(*) from jy_equipment_info where equipment_number=?";
		String [] can=new String[]{beidouId};
		return db.select(sql, can);
	}
	
	public static List<Maps> getMap(String beidouId){
		String sql="select jmd.* from jy_equipment_info jei LEFT JOIN jy_monitor_data jmd on jmd.beidouid=jei.equipment_number where equipment_number=?";
		String [] can=new String[]{beidouId};
		return getMaps(sql, can);
	} 
	
	public static List<Maps> getGps(String beidouId){
		String sql="select * from jy_gps order by gps_time";
		String [] can=new String[]{beidouId};
		return getMaps(sql, can);
	} 
	
	public static int saveGps(String id,String str,String date){
		DBHelper db=new DBHelper();
		String sql="insert into jy_gps VALUES(?,?,?)";
		String[]can=new String[]{id,str,date};
		return db.save(sql, can);
	}
	
	
	public static List<Maps> getMaps(String sql,String [] can){
		long begin = System.currentTimeMillis();
		Connection conn = DBHelper.getConn();
		ResultSet rs=null;
		int num=0;
		List<Maps> maps=new ArrayList<Maps>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=0;i<can.length;i++){
				ps.setString(i+1, can[i]);
			}
			rs=ps.executeQuery();
			if(rs.next()){
				Maps map=new Maps();
				map.setOriginallng(rs.getString("originallng"));
				map.setOriginallat(rs.getString("originallat"));
				map.setNowspace(rs.getString("nowspace"));
				/*map.setLng(rs.getString("lng"));
				map.setLat(rs.getString("lat"));*/
				maps.add(map);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBHelper.closeConn(conn);
		return maps;
	}
	
	
	public static List<String> getGps(String sql,String [] can){
		long begin = System.currentTimeMillis();
		Connection conn = DBHelper.getConn();
		ResultSet rs=null;
		int num=0;
		List<String> maps=new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=0;i<can.length;i++){
				ps.setString(i+1, can[i]);
			}
			rs=ps.executeQuery();
			if(rs.next()){
				maps.add(rs.getString("gps_stage"));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBHelper.closeConn(conn);
		return maps;
	}
}
