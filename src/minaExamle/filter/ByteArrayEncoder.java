package minaExamle.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ByteArrayEncoder extends CumulativeProtocolDecoder   {
	@Override
    public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
        System.out.println("------jinlailes------");
        System.out.println(in!=null);
        if(in.remaining() > 5){//ǰ4�ֽ��ǰ�ͷ
            //��ǵ�ǰposition�Ŀ��ձ��mark���Ա��̵�reset�����ָܻ�positionλ��
            in.mark(); 
            System.out.println("2");
            byte[] l = new byte[5];
            System.out.println("3");
            in.get(l);
            System.out.println("4");
            System.out.println("-------------sdfsdf-----------");
            System.out.println(l.toString());
            //�������ݳ���
            //int len = MyTools.bytes2int(l);//��byteת��int
           
            //ע�������get�����ᵼ�������remaining()ֵ�����仯
        }
        return false;//����ɹ����ø�����н����¸���
    }
}
