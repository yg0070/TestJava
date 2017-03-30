package minaExamle.util;

import java.math.BigDecimal;
import java.util.List;

import minaExamle.model.Maps;

public class BaiDuMap {
	static double DEF_PI = 3.14159265359; // PI
    static double DEF_2PI= 6.28318530712; // 2*PI
    static double DEF_PI180= 0.01745329252; // PI/180.0
    static double DEF_R =6370693.5; // radius of earth
            //适用于近距离
    public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧�?
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度�?
        dew = ew1 - ew2;
        // 若跨东经和西�?80 度，进行调整
        if (dew > DEF_PI)
        dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
        dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance/1000;
    }
            //适用于远距离
    public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double distance;
        // 角度转换为弧�?
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心�?��的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0)
             distance = 1.0;
        else if (distance < -1.0)
              distance = -1.0;
        // 求大圆劣弧长�?
        distance = DEF_R * Math.acos(distance);
        return distance;
    }
    
    public static double licheng(List<Maps> maps){
    	double liCheng=0;
    	double [][]linLi=new double[1][2];
    	for(int i=0;i<maps.size();i++){
    		if(i==0){
    			linLi[0][0]=Double.parseDouble(maps.get(i).getLat());
    			linLi[0][1]=Double.parseDouble(maps.get(i).getLng());
    		}else{
    			
    			double shu1 = BaiDuMap.GetShortDistance(linLi[0][1],linLi[0][0],Double.parseDouble( maps.get(i).getLng()), Double.parseDouble(maps.get(i).getLat()));
    			/*BigDecimal bd = new BigDecimal(shu1);
    	        double shu2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();*/
    			System.out.println("里程::=--------------"+shu1);
    			liCheng+=shu1;
    			linLi[0][	0]=Double.parseDouble(maps.get(i).getLat());
    			linLi[0][1]=Double.parseDouble(maps.get(i).getLng());
    		}
    	}
        BigDecimal bd = new BigDecimal(liCheng);
        liCheng=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    	System.out.println("里程总数:_-------------------"+liCheng+"\t"+maps.size());
    	return liCheng;
    }
    /*
     *  1458C70708F14F13B27907EC96C1C2D5	117.11831133092	36.868025919406		鲁AE8138	687501000003795	0.001125895028445461	2017-01-04 12:34:21.0	2	2	0.0	0.0	2016-01-07 09:16:47.0		117.10573833333333	36.86111833333334	0
		966DF992BAE34F87A059F7173CB3E64D	117.11832323677	36.868022482843		鲁AE8138	687501000003795	0.001163143759728292	2017-01-04 12:34:06.0	2	2	0.0	0.0	2016-01-07 09:16:46.0		117.105745	36.861115000000005	0
		EFB6695838054E5A9A2941ECE05B2D66	117.11831349736	36.868029462791		鲁AE8138	687501000003795	7.941490940749496E-4	2017-01-04 12:33:51.0	2	2	0.0	0.0	2016-01-07 09:16:44.0		117.10573666666666	36.86111666666667	0
		187DBEEC4ED244BDB3359C254FDB6AC4	117.11830645941	36.868025068486		鲁AE8138	687501000003795	0.0	2017-01-04 12:33:36.0	2	2	0.0	0.0	2016-01-07 09:16:41.0		117.10572833333335	36.861115000000005	0

     * */
    public static void main(String[] args) {
    	/*
    	 *  public static void main(String[] args) {
	    	double mLat1 = 39.90923; // point1纬度
	        double mLon1 = 116.357428; // point1经度
	        double mLat2 = 39.90923;// point2纬度
	        double mLon2 = 116.397428;// point2经度
	        double distance = BaiDuMap.GetShortDistance(mLon1, mLat1, mLon2, mLat2);
	        System.out.println(distance);
	    	}
    	 * */
		double lat0=36.868025068486;
		double lon0=117.11830645941;
		double lat1=36.868029462791;
		double lon1=117.11831349736;
		double dou=BaiDuMap.GetShortDistance(lon1,lat1,lon0,lat0);
		System.out.println(dou);
	}
}
