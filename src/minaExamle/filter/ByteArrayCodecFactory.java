package minaExamle.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ByteArrayCodecFactory  implements ProtocolCodecFactory {
	//private ByteArrayDecoder decoder;
    private ByteArrayEncoder encoder;
    
    public ByteArrayCodecFactory() {
    	//decoder = new ByteArrayDecoder();
        encoder = new ByteArrayEncoder();
        
    }

    /*@Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
    	System.out.println("-----encode-----");
        return decoder;
    }
*/
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
    	System.out.println("-----decoder-----");
        return encoder;
    }

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
