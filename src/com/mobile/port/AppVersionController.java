package com.mobile.port;

import com.mobile.service.AppVersionService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @ClassName AppVersionController
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/27 10:15
 * @Version 1.0
 **/
@Controller
public class AppVersionController {
    @Resource(name = "appVersionService")
    private AppVersionService appVersionService;
}
