package cj.netos.chasechain.bubbler;

import java.util.List;

public interface ITrafficPoolService {

    List<String> pageLowerPool(int limit, long offset);

}
