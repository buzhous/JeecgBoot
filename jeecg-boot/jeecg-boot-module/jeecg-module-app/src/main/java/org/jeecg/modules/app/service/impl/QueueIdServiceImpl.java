package org.jeecg.modules.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.QueueId;
import org.jeecg.modules.app.mapper.QueueIdMapper;
import org.jeecg.modules.app.service.IQueueIdService;
import org.springframework.stereotype.Service;


@Service
public class QueueIdServiceImpl extends ServiceImpl<QueueIdMapper, QueueId> implements IQueueIdService {

}
