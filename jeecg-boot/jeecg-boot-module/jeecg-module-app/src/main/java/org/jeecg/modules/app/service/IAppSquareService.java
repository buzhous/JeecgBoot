package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.Topic;
import org.jeecg.modules.app.bean.vo.square.ItemTopicVO;


public interface IAppSquareService extends IService<Topic> {

    /**
     * 分页查询话题
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    IPage<ItemTopicVO> getTopicPage(String searchKey, Integer page, Integer size);

}