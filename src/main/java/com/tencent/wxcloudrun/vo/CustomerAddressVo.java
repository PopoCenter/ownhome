package com.tencent.wxcloudrun.vo;

public class CustomerAddressVo {

    /**
     * id
     */
    private Long id;

    /**
     * 地址信息
     */
    private String address;

    /**
     * 经度
     */
    private String latitude;

    /**
     * 纬度
     */
    private String longitude;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
