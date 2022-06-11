package GamerDao;

import UserDao.Record;

import java.util.List;

/**
 * @author lxl
 */
public interface GamerDao {

    /**
     * 删除接口
     * @param name 玩家名字
     */
    void doDelete(String name);

    /**
     * 添加接口
     * @param gamer 存储的对象
     */
    void doAdd(Gamer gamer);

    /**
     * 查询玩家是否存在
     * @param name 账号名
     * @return 账号，不存在时返回null
     */
    Gamer findByName(String name);

    /**
     * 返回整个排行榜
     * @return 排行榜中所有对象的列表
     */
    List getAll();

    /**
     * 读取排行榜数据文件
     * @param path 文件路径
     */
    void readFile(String path);

    /**
     * 写排行榜数据文件
     * @param path 文件路径
     */
    void writeFile(String path);
}
