package org.jeecg.modules.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.FactoryInfo;
import org.jeecg.modules.app.mapper.FactoryInfoMapper;
import org.jeecg.modules.app.service.IFactoryInfoService;
import org.springframework.stereotype.Service;


@Service
public class FactoryInfoServiceImpl extends ServiceImpl<FactoryInfoMapper, FactoryInfo> implements IFactoryInfoService {

}
