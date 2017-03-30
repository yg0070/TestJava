package minaExamle.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import minaExamle.jdbc.MapsDao;
import minaExamle.model.Maps;
import minaExamle.util.BaiDuMap;
import minaExamle.util.PostUtil;
import minaExamle.util.UUIDUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.mina.core.session.IoSession;

import common.Logger;


public class CrackData {
	public static String str;
	private Maps map;
	private boolean bool;
	public static IoSession out;
	public void begin() throws ParseException{
		System.out.println("----------------------start--------------------------");
		Date date1=new Date();
		System.out.println(str);
		if("*MG20".equals(str.substring(0,5))||"[MG20".equals(str.substring(0,5))){
			//*MG200687501000003217,BA&A0947553657969412041600160108271115&B0100000000&G000460&M990&N31&O0800&T0009#
			map=new Maps();
			map.setBeidouid(str.substring(6,str.indexOf(",")));
			String str2=str.substring(str.indexOf(",")+1);
			branch(str2.substring(0,str2.lastIndexOf("#")));
		}else{
			System.out.println("不是数据包");
		}
		Date date2=new Date();
		System.out.println("结束:"+(date2.getTime()-date1.getTime()));
		//Runtime run = Runtime.getRuntime();
		//double max = run.maxMemory();
		//double total = run.totalMemory();
		//double free = run.freeMemory();
		//double usable = max - total + free;
		/*System.out.println("最大内存 = " + (max/1024/1024)+"m");
		System.out.println("已分配内存 = " + (total/1024/1024)+"m");*/
		//System.out.println("已分配内存中的剩余空间 = " + (free/1024/1024)+"m");
		//System.out.println("最大可用内存 = " + (usable/1024/1024)+"m");
		System.out.println("--------------------------end----------------------------------------");
	}
	public void branch(String brank) throws ParseException{
		Logger logger  =  Logger.getLogger(CrackData.class);
		bool=false;
		//上传状态类信息
		if("A".equals(brank.substring(0,1))){
			//警告
			if("A".equals(brank.substring(1,2))){
				
			}
			//登陆
			else if("B".equals(brank.substring(1,2))){
				System.out.println("登陆包");
				String login="*MG20YAB#";
				out.write(login.getBytes());
				out.write("*MG2011BK04B0#".getBytes());
			}
			//脱网信息
			else if("C".equals(brank.substring(1,2))){
				
			}
			//心跳包
			else if("H".equals(brank.substring(1,2))){
				System.out.println("心跳包");
				String hui="*MG20YAH#";
				out.write(hui.getBytes());
			}
			//上传终端参数
			else if("J".equals(brank.substring(1,2))){
				System.out.println("上传终端参数");
			}
			//查询 OBD 相关参数
			else if("K".equals(brank.substring(1,2))){
				
			}
			//上传进出范围报警信息
			else if("N".equals(brank.substring(1,2))){
				
			}
			//.......................................................等等
		}
		//上传定位类信息
		else if("B".equals(brank.substring(0,1))){
			JSONObject jsonobjs=new JSONObject();
			jsonobjs.put("params", "1");
			JSONArray jsons=new JSONArray();
			jsons.add(jsonobjs);
			System.out.println(brank);
			String[]shuzu=brank.substring(3).split("&");
			for (int i = 0; i < shuzu.length; i++) {
				//gps数据包
				if("A".equals(shuzu[i].substring(0,1))){
					gpsData(shuzu[i]);
				}
				//状态及报警编码
				if("B".equals(shuzu[i].substring(0,1))){
				}
				//里程数
				/*if("C".equals(shuzu[i].substring(0,1))){
					//里程shuzu[i].substring(1)
					double nowspace=(new asStringToasInt().t2(shuzu[i].substring(1)))/3600*2*1.852;
					map.setNowspace(nowspace+"");
					bool=true;
				}*/
			}
			System.out.println("设备："+map.getBeidouid()+"\n时间："+map.getNowdate()+"\n经度："+map.getLng()+"\n纬度"+map.getLat()
					+"\n速度："+map.getGpsstate()+"\n方向："+map.getAngle());
			/*if(bool){
				logger.error("设备:"+map.getBeidouid()+"\t里程数"+map.getNowspace());
			}*/
			if(saveDate(map)>=1){
				//PostUtil.doPost("http://localhost:8080/truck_cloud/dataHandling.do?method=getDate", jsons);
				//PostUtil.doPost("http://182.92.4.57:8080/truck_cloud/dataHandling.do?method=getDate", jsons);
				//PostUtil.doPost("http://172.22.28.193:8088/truck_cloud/dataHandling.do?method=getDate", jsons);
				PostUtil.doPost("http://localhost:8088/truck_cloud/dataHandling.do?method=getDate", jsons);
				System.out.println("保存成功");
			}else{
				System.out.println("保存失败");
			}
		}
		//动态加载
		else if("D".equals(brank.substring(0,1))){
			
		}
		//短语信息透明传输
		else if("G".equals(brank.substring(0,1))){
			
		}
	}
	//gps定时回传的定位数据包
	public void gpsData(String data) throws ParseException{
		//gps定位数据z
		String linshi=data.substring(1);
		String jing=linshi.substring(6,14);
		String wei=linshi.substring(14,23);
		//经度
		Double lng=(Double.parseDouble(wei.substring(0,3))*60+Double.parseDouble(wei.substring(3,5)+"."+wei.substring(5,wei.length())))/60;
		map.setLng(lng+"");
		//纬度
		Double lat=(Double.parseDouble(jing.substring(0,2))*60+Double.parseDouble(jing.substring(2,4)+"."+jing.substring(4,jing.length())))/60;
		map.setLat(lat+"");
		//时间日期
		String shi=linshi.substring(0,6);
		String da=linshi.substring(28);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer shijian=new StringBuffer((Integer.parseInt(shi.substring(0,2)))+":"+shi.substring(2,4)+":"+shi.substring(4,6));
		StringBuffer date=new StringBuffer("20"+da.substring(4,6)+"-"+da.substring(2,4)+"-"+da.substring(0,2));
		map.setNowdate(format.format(format.parse(date+" "+shijian)));
		//保存时间
		map.setInssj(format.format(new Date()));
		//速度
		double sudu=Double.parseDouble(linshi.substring(24,26))*2*1.852;
		if(sudu==0){
			map.setState("2");
			map.setGpsstate("0.0");
		}else{
			map.setState("1");
			map.setGpsstate(Math.round(sudu)+"");
		}
		//String weizhi=linshi.substring(23,24);
		//方向
		double fang=10*(Double.parseDouble(linshi.substring(26,28)));
		map.setAngle(fang+"");
		String jing2=jing.substring(0,2)+"°"+jing.substring(2,4)+"."+jing.substring(4,jing.length());
		String wei2=wei.substring(0,3)+"°"+wei.substring(3,5)+"."+wei.substring(5,wei.length());
		System.out.println("经度"+jing2+"\t纬度："+wei2);
	}
	//保存数据库
	public static int saveDate(Maps map){
		if(MapsDao.getCar(map.getBeidouid())>=1){
			map.setMonitorid(new UUIDUtils().getUUID());
			List<Maps> maps=MapsDao.getMap(map.getBeidouid());
			double licheng=0;
			System.out.println(maps.size());
			if(maps.size()!=0){
				/*String url = "http://api.map.baidu.com/geoconv/v1/?coords="+map.getLng()+","+map.getLat()+"&from=1&to=5&ak=uUKa2OD6ifuT4SPsIc6pwLtM";
				String js = loadJSON(url);
			    JSONObject obj = JSONObject.fromObject(js);
			    String lng="";
			    String lat="";
			    if(obj.get("status").toString().equals("0")){
			    	lng=obj.getJSONArray("result").getJSONObject(0).getString("x");
			    	lat=obj.getJSONArray("result").getJSONObject(0).getString("y");
			    }
			    System.out.println(map.getNowdate()+"\t"+lng+"\t"+maps.get(0).getLng()+"\t"+lat+"\t"+maps.get(0).getLat());
			    licheng=BaiDuMap.GetShortDistance(Double.parseDouble(lng),Double.parseDouble(lat),Double.parseDouble(maps.get(0).getLng()),Double.parseDouble(maps.get(0).getLat()));
			    
			    System.out.println(map.getNowdate()+"\t"+map.getLng()+"\t"+maps.get(0).getLng()+"\t"+map.getLat()+"\t"+maps.get(0).getLat());
				licheng=BaiDuMap.GetShortDistance(Double.parseDouble(map.getLng()),Double.parseDouble(map.getLat()),Double.parseDouble(maps.get(0).getLng()),Double.parseDouble(maps.get(0).getLat()));
			    
			    licheng=BaiDuMap.GetShortDistance(Double.parseDouble(maps.get(0).getOriginallng()),Double.parseDouble(maps.get(0).getOriginallat()),Double.parseDouble(map.getLng()),Double.parseDouble(map.getLat()));
			    */
				//System.out.println(maps.get(0).getOriginallng()+"\t"+maps.get(0).getOriginallat()+"\t"+map.getNowdate()+"\t"+map.getLng()+"\t"+map.getLat());
				if(maps.get(0).getNowspace()!=null&&!"".equals(maps.get(0).getNowspace())){
					//System.out.println("----------相加啦---------"+maps.get(0).getNowspace()+"sss");
					licheng=BaiDuMap.GetShortDistance(Double.parseDouble(map.getLng()),Double.parseDouble(map.getLat()),Double.parseDouble(maps.get(0).getOriginallng()),Double.parseDouble(maps.get(0).getOriginallat()));
					licheng+=Double.parseDouble(maps.get(0).getNowspace());
				}
			}
			map.setNowspace(licheng+"");
			/*if("687501000003795".equals(map.getBeidouid())){
				//new MapsDao().saveGps(new UUIDUtils().getUUID(),str,map.getNowdate());
			}*/
			System.out.println("里程:"+map.getNowspace());
			int i=MapsDao.saveMaps(map);
			return i;
		}else{
			System.out.println("--------设备没有绑定，请先绑定--------");
			return 0;
		}
	}
	public static String loadJSON (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return json.toString();
    }

	/*public static void main(String[] args) {
		String url = "http://api.map.baidu.com/geoconv/v1/?coords="+119.87245166666666+","+36.36276333333333+"&from=1&to=5&ak=uUKa2OD6ifuT4SPsIc6pwLtM";
		String js = loadJSON(url);
	    JSONObject obj = JSONObject.fromObject(js);
	    if(obj.get("status").toString().equals("0")){
	    	String lng=obj.getJSONArray("result").getJSONObject(0).getString("x");
	    	String lat=obj.getJSONArray("result").getJSONObject(0).getString("y");
        	System.out.println(lng+"\n"+lat);
	    }
	}*/

}
