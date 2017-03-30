package minaExamle.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ByteArrayDecoder extends ProtocolEncoderAdapter {
	@Override
    public void encode(IoSession session, Object message,
            ProtocolEncoderOutput out) throws Exception {
        out.write(message);
        out.flush();
        
    }
}
