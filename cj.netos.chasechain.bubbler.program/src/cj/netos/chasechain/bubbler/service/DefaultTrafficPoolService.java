package cj.netos.chasechain.bubbler.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.netos.chasechain.bubbler.ITrafficPoolService;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CjService(name = "defaultTrafficPoolService")
public class DefaultTrafficPoolService implements ITrafficPoolService {

    @CjServiceRef(refByName = "mongodb.netos.home")
    ICube home;

    @Override
    public List<String> pageLowerPool(int limit, long offset) {
        String cjql = String.format("select {'tuple.id':1}.limit(%s).skip(%s) from tuple chasechain.traffic.pools %s where {'$or':[{'tuple.level':-1},{'tuple.level':4}]}",
                limit, offset, HashMap.class.getName());
        IQuery<Map<String, String>> query = home.createQuery(cjql);
        List<IDocument<Map<String, String>>> list = query.getResultList();
        List<String> ids = new ArrayList<>();
        for (IDocument<Map<String, String>> document : list) {
            ids.add(document.tuple().get("id"));
        }
        return ids;
    }
}
