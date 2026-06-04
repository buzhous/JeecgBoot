package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.OssFiles;


public interface IAppMediaService extends IService<OssFiles> {

    /**
     * 保存多媒体文件
     */
    boolean saveMedia(OssFiles files);
}
