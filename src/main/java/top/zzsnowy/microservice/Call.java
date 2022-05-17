package top.zzsnowy.microservice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.zzsnowy.model.Node;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Call {

    Map<Node, Integer> API2Map = new HashMap<>();
    Map<Node, Integer> Cell2Map = new HashMap<>();
    Map<Node, Integer> APICellMap = new HashMap<>();
    Map<Node, Integer> SameMSAPI2Map = new HashMap<>();

}
