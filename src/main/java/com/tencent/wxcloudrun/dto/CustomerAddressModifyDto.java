package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class CustomerAddressModifyDto {

    @NotNull(groups = {CustomerAddressModifyDto.Modify.class}, message = "id不能为空")
    private Long id;

    private String address;

    /**
     * 经度
     */
    private String latitude;

    /**
     * 纬度
     */
    private String longitude;


    public interface Modify {
    }


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
