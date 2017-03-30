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
        if(in.remaining() > 5){//前4字节是包头
            //标记当前position的快照标记mark，以便后继的reset操作能恢复position位置
            in.mark(); 
            System.out.println("2");
            byte[] l = new byte[5];
            System.out.println("3");
            in.get(l);
            System.out.println("4");
            System.out.println("-------------sdfsdf-----------");
            System.out.println(l.toString());
            //包体数据长度
            //int len = MyTools.bytes2int(l);//将byte转成int
           
            //注意上面的get操作会导致下面的remaining()值发生变化
        }
        return false;//处理成功，让父类进行接收下个包
    }
}
