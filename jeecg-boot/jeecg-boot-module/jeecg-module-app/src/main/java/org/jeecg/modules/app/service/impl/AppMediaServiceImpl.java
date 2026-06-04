package org.jeecg.modules.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.mapper.OssFilesMapper;
import org.jeecg.modules.app.service.IAppMediaService;
import org.jeecg.modules.app.entity.OssFiles;
import org.springframework.stereotype.Service;


@Service
public class AppMediaServiceImpl extends ServiceImpl<OssFilesMapper, OssFiles> implements IAppMediaService {

    @Override
    public boolean saveMedia(OssFiles files) {

        return this.save(files);
    }


}
