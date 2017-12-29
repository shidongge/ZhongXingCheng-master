package us.mifeng.zhongxingcheng.bean;

import java.util.List;

/**
 * Created by shido on 2017/11/2.
 */

public class Home_ShangPinBean {


    /**
     * id : 1
     * name : 幽兰茶上客
     * picture : userShops/20170814/1502712437111.png
     * goods : [{"goodsId":14,"goodsName":"幽兰茶上客-2015年荒野牡丹-珍藏福鼎白茶-茶饼350克","imgCart":"goodsThumb/20170812/150253713848.jpg"},{"goodsId":24,"goodsName":"幽兰茶上客-精品安吉白茶250克-礼盒装5盒装珍惜白茶叶","imgCart":"goodsThumb/20170812/150253703850.jpg"},{"goodsId":25,"goodsName":"幽兰茶上客-陈年私定2011年老白茶贡眉-礼盒装2斤茶磚","imgCart":"goodsThumb/20170812/150253671130.jpg"}]
     */

    private String id;
    private String name;
    private String picture;
    private List<GoodsBean> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * goodsId : 14
         * goodsName : 幽兰茶上客-2015年荒野牡丹-珍藏福鼎白茶-茶饼350克
         * imgCart : goodsThumb/20170812/150253713848.jpg
         */

        private String goodsId;
        private String goodsName;
        private String imgCart;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getImgCart() {
            return imgCart;
        }

        public void setImgCart(String imgCart) {
            this.imgCart = imgCart;
        }
    }
}
