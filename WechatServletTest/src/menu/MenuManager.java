package menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.AccessToken;
import common.WeixinUtil;


public class MenuManager {
    private static Logger log = LoggerFactory.getLogger(MenuManager.class);

    public static void main(String[] args) {
        // 第三方用户唯一凭证
        String appId = "wx97c110f9eb3fe0eb";
        // 第三方用户唯一凭证密钥
        String appSecret = "539041dca8d1cc8d2be1b41c70495978";

        // 调用接口获取access_token
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (null != at) {
            // 调用接口创建菜单
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());

            // 判断菜单创建结果
            if (0 == result)
                log.info("菜单创建成功！");
            else
                log.info("菜单创建失败，错误码：" + result);
        }
    }

    /**
     * 组装菜单数据
     * 
     * @return
     */
    private static Menu getMenu() {
        CommonButton btn11 = new CommonButton();
        btn11.setName("景区介绍");
        btn11.setType("click");
        btn11.setKey("11");

        CommonButton btn12 = new CommonButton();
        btn12.setName("景区地图");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButton btn21 = new CommonButton();
        btn21.setName("天气查询");
        btn21.setType("click");
        btn21.setKey("21");

        CommonButton btn22 = new CommonButton();
        btn22.setName("票务查询");
        btn22.setType("click");
        btn22.setKey("22");

        CommonButton btn23 = new CommonButton();
        btn23.setName("推荐路线");
        btn23.setType("click");
        btn23.setKey("23");

        CommonButton btn24 = new CommonButton();
        btn24.setName("周边美食");
        btn24.setType("click");
        btn24.setKey("24");

        CommonButton btn31 = new CommonButton();
        btn31.setName("实时泊位");
        btn31.setType("click");
        btn31.setKey("31");

        CommonButton btn32 = new CommonButton();
        btn32.setName("爱车查找");
        btn32.setType("click");
        btn32.setKey("32");


        
        /**
         * 微信：  mainBtn1,mainBtn2,mainBtn3底部的三个一级菜单。
         */
        
        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("微景区");
        //一级下有4个子菜单
        mainBtn1.setSub_button(new CommonButton[] { btn11, btn12 });

        
        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("微服务");
        mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24 });

        
        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("微停车");
        mainBtn3.setSub_button(new CommonButton[] { btn31, btn32 });

        
        /**
         * 封装整个菜单
         */
        Menu menu = new Menu();
        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

        return menu;
    }
}
