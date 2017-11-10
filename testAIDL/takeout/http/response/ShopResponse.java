package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.Shop;
import com.yunnex.vpay.lib.http.ResponseBase;

/**
 * Created by sungongyan on 2016/1/6.
 * wechat sun379366152
 */
public class ShopResponse extends ResponseBase {

    private ShopResponseDetail response;

    public ShopResponseDetail getResponse()
    {
        return response;
    }

    public void setResponse(ShopResponseDetail response)
    {
        this.response = response;
    }

    public class ShopResponseDetail{
        private Shop shop;

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }
    }
}
