package Search_result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.activation.MailcapCommandMap;

import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.log4j.lf5.viewer.TrackingAdjustmentListener;
import org.codehaus.jackson.map.util.Comparators;

import com.github.javaparser.utils.SourceRoot.Callback.Result;

import method_analyze.Id2methodBody;
import nilsimsa.Optimize;

public class SearchResult {
	
	public static void main(String[] args) {
		String code = "{\r\n" + 
				"    return ifNameIs(\"ville\").andArgIs(0, \"cede\").itMatches();\r\n" + 
				"}";
		SearchResult searchResult = new SearchResult();
		searchResult.prepareOptandId2m();
		Set<String[]> set = searchResult.search(code).get("jsteenbeeke/Jaybukkit").get("master_20180129");
		for(String[] s:set) {
			System.out.println(s[0]+"---"+s[1]);
			System.out.println(searchResult.searchCode(Integer.parseInt(s[1])));
		}
		
		
		searchResult.close();
	}
	
	public SearchResult() {
		id2methodBody = new Id2methodBody();
	}
	
	private static Optimize optimize = null;
	private Id2methodBody id2methodBody = null;
	// {repo[release[methodinfo&id]], ...}
	private HashMap<String,Map<String, Set<String[]>>> result = null;
	
	public HashMap<String,Map<String, Set<String[]>>> search(String code) {
		Set<String> briefResult = optimize.findBriefInfo(code, true);
		result = new HashMap<String,Map<String, Set<String[]>>>();
		
		for(String r:briefResult) {
			//System.out.println(r);
			// repo,release,javafile,classname,signature,id
			String[] infos = r.split("#");
			// if this repo is contained
			if(result.containsKey(infos[0])) {
				// if this release is contained
				if(result.get(infos[0]).containsKey(infos[1])) {
					String[] tem = new String[2];
					tem[0] = infos[2]+","+infos[3]+","+infos[4];
					tem[1] = infos[5];
					result.get(infos[0]).get(infos[1]).add(tem);
				}else {
					String[] tem = new String[2];
					tem[0] = infos[2]+","+infos[3]+","+infos[4];
					tem[1] = infos[5];
					Set<String[]> teSet = new HashSet<String[]>();
					teSet.add(tem);
					result.get(infos[0]).put(infos[1], teSet);
				}
			}else {
				String[] tem = new String[2];
				tem[0] = infos[2]+","+infos[3]+","+infos[4];
				tem[1] = infos[5];
				Set<String[]> teSet = new HashSet<String[]>();
				teSet.add(tem);
				HashMap<String, Set<String[]>> teMap = new HashMap<String, Set<String[]>>();
				teMap.put(infos[1], teSet);
				result.put(infos[0], teMap);
			}
		}
		
		
		return result;
		
	}
	
	
	public static Map<String,Map<String, Set<String[]>>> sort(HashMap<String,Map<String, Set<String[]>>> result){
		// 对release按日期排序
		Map<String, Set<String[]>> myRepoMap = new LinkedHashMap<String, Set<String[]>>();
		//System.out.println("result:"+result);
		for(Entry<String, Map<String, Set<String[]>>> entry:result.entrySet()) {
			List<Map.Entry<String, Set<String[]>>> entry_list = new ArrayList<Map.Entry<String,Set<String[]>>>(entry.getValue().entrySet());
			Collections.sort(entry_list, new Comparator<Map.Entry<String, Set<String[]>>>()
			{
				public int compare(Map.Entry<String, Set<String[]>> o1, Map.Entry<String, Set<String[]>> o2)
				{
					return o1.getKey().split("/")[1].compareTo(o2.getKey().split("/")[1]);
				}
			});
			//System.out.println("entrylist:"+entry_list);
			Iterator<Map.Entry<String, Set<String[]>>> iterator = entry_list.iterator();
			while (iterator.hasNext()) {
				Entry<String, Set<String[]>> repo = iterator.next();
				String key = repo.getKey();
				myRepoMap.put(key, entry.getValue().get(key));
			}
			result.put(entry.getKey(), myRepoMap);
			myRepoMap = new LinkedHashMap<String, Set<String[]>>();
			//System.out.println("result:"+result);
		}
		
		// 对整个结果按实例数量倒排序
		List<Map.Entry<String,Map<String, Set<String[]>>>> list_data = new ArrayList<Map.Entry<String,Map<String, Set<String[]>>>>(result.entrySet());
		Collections.sort(list_data, new Comparator<Map.Entry<String,Map<String, Set<String[]>>>>()
		{
			public int compare(Map.Entry<String,Map<String, Set<String[]>>> o1, Map.Entry<String,Map<String, Set<String[]>>> o2)
			{
				return getSetSize(o2.getValue())-getSetSize(o1.getValue());
			}
		});
		//linkdhashmap是有序的
		Map<String,Map<String, Set<String[]>>> myMap = new LinkedHashMap<String,Map<String, Set<String[]>>>();
		Iterator<Map.Entry<String,Map<String, Set<String[]>>>> iter = list_data.iterator();
		while(iter.hasNext()) {
			Entry<String, Map<String, Set<String[]>>> tem = iter.next();
			String key = tem.getKey();
			myMap.put(key, result.get(key));
		}
		return myMap;
	}
	
	
	private static int getSetSize(Map<String, Set<String[]>> h) {
		Iterator<Entry<String, Set<String[]>>> iter = h.entrySet().iterator();
		int size = 0;
		while(iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			Set<String[]> val = (Set<String[]>) entry.getValue();
			size += val.size();
		}
		return size;
	}
	
	
	public List<String> detail(List<Integer> idList) {
		List<String> result = new LinkedList<String>();
		for(int i:idList) {
			//System.out.println(searchCode(i));
			//result.add(searchCode(i).replace("\n", "").replace("\r", "").replace("\\", ""));
			result.add(searchCode(i));
			
		}
		//System.out.println(result.toString());
		return result;
	}
	
	
	public void prepareOptandId2m() {
		optimize = new Optimize();
		////////////////////////////////
		/**   this will be modified   */
		//optimize.prepare("D:\\java_\\CodeClone\\hash.txt");
		//optimize.prepare("/home/fdse/user/fwk/hash.txt");
		
	}
	
	
	public String searchCode(int id) {
		return id2methodBody.getMethodById(id);
	}
	
	public void close() {
		
		id2methodBody.closeConn();
	}
	
}

