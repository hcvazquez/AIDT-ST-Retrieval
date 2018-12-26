package external.wrappers;

import java.net.Proxy;
import java.util.List;

import metasearch.Searcher;

public abstract class SearchWrapperAbs implements SearchWrapper {

    public abstract List<String> downloadResultContent(String query, Proxy proxy);
    
    public List<String> getResultContent(String query, Proxy proxy, Searcher searcher){
        /*List<String> contents = CacheContentManager.getInstance().loadContentFromCache(searcher,query);
        if(contents!=null){
            return contents;
        }else{
            contents = downloadResultContent(query, proxy);
            return contents;
        }*/    
        return downloadResultContent(query, proxy);
    }
}
