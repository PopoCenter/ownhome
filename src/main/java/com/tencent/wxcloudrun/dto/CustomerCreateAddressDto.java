package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class CustomerCreateAddressDto {

    private String address;

    /**
     * 经度
     */
    private String latitude;

    /**
     * 纬度
     */
    private String longitude;


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
