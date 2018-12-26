package external.wrappers;

import java.util.List;
import java.net.Proxy;

public interface SearchWrapper {

    public List downloadResultContent(String query, Proxy proxy);

}
