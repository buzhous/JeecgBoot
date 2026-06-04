package org.jeecg.modules.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.SendSmsLog;
import org.jeecg.modules.app.mapper.SendSmsLogMapper;
import org.jeecg.modules.app.service.ISendSmsLogService;
import org.springframework.stereotype.Service;


@Service
public class SendSmsLogServiceImpl extends ServiceImpl<SendSmsLogMapper, SendSmsLog> implements ISendSmsLogService {

    @Override
    public boolean saveSmsLog(SendSmsLog sendSmsLog) {
        return this.save(sendSmsLog);
    }

}
