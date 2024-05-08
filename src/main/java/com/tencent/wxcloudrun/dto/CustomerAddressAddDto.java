package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class CustomerAddressAddDto {

    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "customerId不能为空")
    private Long customerId ;

    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "customerId不能为空")
    @NotBlank(groups = {CustomerCreateDto.Add.class}, message = "customerId不能为空")
    private String address;

    /**
     * 经度
     */
    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "customerId不能为空")
    @NotBlank(groups = {CustomerCreateDto.Add.class}, message = "customerId不能为空")
    private String latitude;

    /**
     * 纬度
     */
    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "customerId不能为空")
    @NotBlank(groups = {CustomerCreateDto.Add.class}, message = "customerId不能为空")
    private String longitude;


    public interface Add {
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
