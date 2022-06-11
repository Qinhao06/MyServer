

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface DAO {
    /**
     * 删除接口
     * @return null
     */
    void doDelete();
    /**
     * 添加接口
     * @param path 文件路径
     * @param data 数据对象数组
     */
    //void doAdd(List<Record> data, String path) throws IOException;

    void doAdd(List<Record> data, String path) throws IOException;

    /**
     * 查询所有数据接口
     * @param path 文件路径
     */
    List getAll(String path) throws IOException, ClassNotFoundException;
}
