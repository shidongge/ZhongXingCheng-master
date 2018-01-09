package us.mifeng.zhongxingcheng.bean;

import java.util.List;

/**
 * Created by shido on 2018/1/9.
 */

public class ShouCangBean {

    /**
     * shopsCount : 3
     * goodsCount : 2
     * goodsCollection : [{"id":"23","type":"1","shopId":"0","goodsId":"31","userId":"20016","addTime":"1515123562","updTime":"1515123567","status":"1","goodsName":"幽兰茶上客-竹叶青茶特级（品味级）绿茶茶叶-礼盒装500克4盒","shortDesc":"竹叶青茶特级（品味级）绿茶茶叶-礼盒装","imgUrl":"http://47.94.144.186:8080/uploadsgoodsThumb/20170814/150269202491.jpg","goodsMoney":"1036.80"},{"id":"27","type":"1","shopId":"0","goodsId":"23","userId":"20016","addTime":"1515141996","updTime":"1515398764","status":"1","goodsName":"本草天香500ml*2冷榨油茶籽油礼盒（有机）","shortDesc":"500ml*2冷榨油茶籽油礼盒（有机）","imgUrl":"http://47.94.144.186:8080/uploadsgoodsThumb/20170812/150253633735.jpg","goodsMoney":"627.84"}]
     * shopsCollection : [{"id":"9","type":"2","shopId":"9","goodsId":"0","userId":"20016","addTime":"1514876976","updTime":"0","status":"1","shopName":"陇上农庄旗舰店","imgIcon":"http://47.94.144.186:8080/uploadsuserShops/20170814/150268341297.jpg","sellCount":"2","goodsCount":"10"},{"id":"24","type":"2","shopId":"1","goodsId":"0","userId":"20016","addTime":"1515123591","updTime":"1515123707","status":"1","shopName":"幽兰茶上客","imgIcon":"http://47.94.144.186:8080/uploadsuserShops/20170814/150271243711.jpg","sellCount":"0","goodsCount":"20"},{"id":"25","type":"2","shopId":"5","goodsId":"0","userId":"20016","addTime":"1515138876","updTime":"0","status":"1","shopName":"手机哥俱乐部","imgIcon":"http://47.94.144.186:8080/uploadsuserShops/20170814/150268328133.jpg","sellCount":"0","goodsCount":"15"}]
     */

    private int shopsCount;
    private int goodsCount;
    private List<GoodsCollectionBean> goodsCollection;
    private List<ShopsCollectionBean> shopsCollection;

    public int getShopsCount() {
        return shopsCount;
    }

    public void setShopsCount(int shopsCount) {
        this.shopsCount = shopsCount;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public List<GoodsCollectionBean> getGoodsCollection() {
        return goodsCollection;
    }

    public void setGoodsCollection(List<GoodsCollectionBean> goodsCollection) {
        this.goodsCollection = goodsCollection;
    }

    public List<ShopsCollectionBean> getShopsCollection() {
        return shopsCollection;
    }

    public void setShopsCollection(List<ShopsCollectionBean> shopsCollection) {
        this.shopsCollection = shopsCollection;
    }

    public static class GoodsCollectionBean {
        /**
         * id : 23
         * type : 1
         * shopId : 0
         * goodsId : 31
         * userId : 20016
         * addTime : 1515123562
         * updTime : 1515123567
         * status : 1
         * goodsName : 幽兰茶上客-竹叶青茶特级（品味级）绿茶茶叶-礼盒装500克4盒
         * shortDesc : 竹叶青茶特级（品味级）绿茶茶叶-礼盒装
         * imgUrl : http://47.94.144.186:8080/uploadsgoodsThumb/20170814/150269202491.jpg
         * goodsMoney : 1036.80
         */

        private String id;
        private String type;
        private String shopId;
        private String goodsId;
        private String userId;
        private String addTime;
        private String updTime;
        private String status;
        private String goodsName;
        private String shortDesc;
        private String imgUrl;
        private String goodsMoney;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getUpdTime() {
            return updTime;
        }

        public void setUpdTime(String updTime) {
            this.updTime = updTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getShortDesc() {
            return shortDesc;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getGoodsMoney() {
            return goodsMoney;
        }

        public void setGoodsMoney(String goodsMoney) {
            this.goodsMoney = goodsMoney;
        }
    }

    public static class ShopsCollectionBean {
        /**
         * id : 9
         * type : 2
         * shopId : 9
         * goodsId : 0
         * userId : 20016
         * addTime : 1514876976
         * updTime : 0
         * status : 1
         * shopName : 陇上农庄旗舰店
         * imgIcon : http://47.94.144.186:8080/uploadsuserShops/20170814/150268341297.jpg
         * sellCount : 2
         * goodsCount : 10
         */

        private String id;
        private String type;
        private String shopId;
        private String goodsId;
        private String userId;
        private String addTime;
        private String updTime;
        private String status;
        private String shopName;
        private String imgIcon;
        private String sellCount;
        private String goodsCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getUpdTime() {
            return updTime;
        }

        public void setUpdTime(String updTime) {
            this.updTime = updTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getImgIcon() {
            return imgIcon;
        }

        public void setImgIcon(String imgIcon) {
            this.imgIcon = imgIcon;
        }

        public String getSellCount() {
            return sellCount;
        }

        public void setSellCount(String sellCount) {
            this.sellCount = sellCount;
        }

        public String getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(String goodsCount) {
            this.goodsCount = goodsCount;
        }
    }
}
