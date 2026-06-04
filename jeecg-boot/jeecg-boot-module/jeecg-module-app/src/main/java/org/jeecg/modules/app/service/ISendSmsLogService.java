package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.SendSmsLog;


public interface ISendSmsLogService extends IService<SendSmsLog> {


    public boolean saveSmsLog(SendSmsLog sendSmsLog);

}
