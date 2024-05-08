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
public class CustomerCreateDto {

    /**
     * 用户名称
     */
    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "名称不能为空")
    @NotBlank(groups = {CustomerCreateDto.Add.class}, message = "名称不能为空")
    private String name;

    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "phone不能为空")
    @NotBlank(groups = {CustomerCreateDto.Add.class}, message = "phone不能为空")
    private String phone;


    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "genderType不能为空")
    private Integer genderType;

    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "ageRange不能为空")
    @NotBlank(groups = {CustomerCreateDto.Add.class}, message = "ageRange不能为空")
    private String ageRange;

    @NotNull(groups = {CustomerCreateDto.Add.class}, message = "addressList不能为空")
    private List<CustomerCreateAddressDto> addressList;


    public interface Add {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGenderType() {
        return genderType;
    }

    public void setGenderType(Integer genderType) {
        this.genderType = genderType;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public List<CustomerCreateAddressDto> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<CustomerCreateAddressDto> addressList) {
        this.addressList = addressList;
    }
}
