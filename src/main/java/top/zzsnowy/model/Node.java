package top.zzsnowy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node {//Node没有重写hashcode，保证读入的依赖不会被覆盖
    String source;
    String target;
}
