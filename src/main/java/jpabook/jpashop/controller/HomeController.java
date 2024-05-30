package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // 롬복으로 로거 사용, log. 으로 사용 가능
public class HomeController {


    @RequestMapping("/")
    public String home() {
        log.info("home controller");
        return "home";
        // home으로 보내줄 것이므로 resources -> templates에 home.html 생성
        // (타임리프 템플릿)

    }
}
