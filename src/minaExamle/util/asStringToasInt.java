package minaExamle.util;

public class asStringToasInt {
	public int t2(String str){//�ַ���ת��ΪASCII��
		  //str="C001;?0;=";//�ַ���
		  char[]chars=str.toCharArray(); //���ַ���ת��Ϊ�ַ����� 
		  String num="";
		  for(int i=3;i<8;i++){//������
			  int j=(int)chars[i];
			  String lin=Integer.toHexString(j);
			  String b=lin.substring(1,2);
/*			  System.out.print(b+"\t"+lin+"\t");
		      System.out.println(" "+chars[i]+Integer.valueOf(b,16));*/
		      num+=b;
		  }
/*		  System.out.println(num);
		  System.out.println(Integer.valueOf(num,16));*/
		  return Integer.valueOf(num,16);
	}
	/*public static void main(String[] args) {
		new asStringToasInt().t2("w");
	}*/
	//ASCIIת��Ϊ�ַ���
	public static void t1(){
		String s="22307 35806 24555 20048";
		String[]chars=s.split(" ");
		System.out.println("ASCII ���� \n----------------------");
		for(int i=0;i<chars.length;i++){ 
			System.out.println(chars[i]+" "+(char)Integer.parseInt(chars[i]));
		} 
	}
}