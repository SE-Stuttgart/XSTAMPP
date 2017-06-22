package xstampp.astpa.model.linking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import xstampp.model.ObserverValue;

class Adapter extends XmlAdapter<ListOfLinks, Map<ObserverValue, List<Link>>> {

  @Override
  public Map<ObserverValue, List<Link>> unmarshal(ListOfLinks loe) throws Exception {
    Map<ObserverValue, List<Link>> map = new HashMap<>();
    for (Entry entry : loe.getList()) {
      map.put(entry.getKey(), entry.getList());
    }
    return map;
  }

  @Override
  public ListOfLinks marshal(Map<ObserverValue, List<Link>> map) throws Exception {
    ListOfLinks loe = new ListOfLinks();
    for (Map.Entry<ObserverValue, List<Link>> mapEntry : map.entrySet()) {
      Entry entry = new Entry();
      entry.setKey(mapEntry.getKey());
      entry.getList().addAll(mapEntry.getValue());
      loe.getList().add(entry);
    }
    return loe;
  }
}