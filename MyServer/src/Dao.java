

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lxl
 */
public interface Dao {
    /**
     * 删除接口
     */
    void doDelete();

    /**
     * 添加接口
     * @param record 存储的对象
     */
    void doAdd(Record record);

    /**
     * 查询是否账号存在
     * @param name 账号名
     * @return 账号，不存在时返回null
     */
    Record findByName(String name);

    /**
     * 查询密码是否正确
     * @param record 查询到的账号信息
     * @param password 密码名
     * @return 密码是否正确
     */
    boolean compareByPassword(Record record, String password);

    /**
     * 读取账号密码数据文件
     * @param path 文件路径
     */
    void readFile(String path);

    /**
     * 写账号密码数据文件
     * @param path 文件路径
     */
    void writeFile(String path);
}
