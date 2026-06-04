package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.app.entity.Topic;
import org.jeecg.modules.app.bean.vo.square.ItemTopicVO;
import org.jeecg.modules.app.mapper.TopicMapper;
import org.jeecg.modules.app.service.IAppSquareService;
import org.jeecg.modules.app.service.ITopicService;
import org.jeecg.modules.app.utils.FieldElementMockUtil;
import org.jeecg.modules.app.utils.FieldElementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AppSquareServiceImpl extends ServiceImpl<TopicMapper, Topic> implements IAppSquareService {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private ITopicService topicService;

    @Override
    public IPage<ItemTopicVO> getTopicPage(String searchKey, Integer page, Integer size) {
        // 创建分页对象
        IPage<Topic> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(searchKey)) {
            queryWrapper.like(Topic::getTopicDesc, searchKey)
                    .or().like(Topic::getTopicName, searchKey);
        }
        // 执行分页查询
        IPage<Topic> iPage = topicService.page(pageObj, queryWrapper);

        // 重新处理分页，并返回ItemTopicVO
        List<ItemTopicVO> topicList = new ArrayList<>();
        iPage.getRecords().forEach(item -> {
            ItemTopicVO itemTopicVO = BeanUtil.copyProperties(item, ItemTopicVO.class);
            // 解析字段元素
            FieldElementUtil.convertToJsonFields(item.getFields(), itemTopicVO);
            // 填充默认值
            itemTopicVO.setTopicIcon(item.getTopicIcon() != null ? item.getTopicIcon() : "");
            itemTopicVO.setTopicName(item.getTopicName() != null ? item.getTopicName() : "");
            itemTopicVO.setTopicDesc(item.getTopicDesc() != null ? item.getTopicDesc() : "");

            // 模拟数据
            itemTopicVO.setFields(FieldElementMockUtil.mockElementFieldElementBase());

            topicList.add(itemTopicVO);
        });

        IPage<ItemTopicVO> topicPage = new Page<>(iPage.getCurrent(), iPage.getSize(), iPage.getTotal());
        topicPage.setRecords(topicList);
        topicPage.setPages(iPage.getPages());

        return topicPage;
    }

}