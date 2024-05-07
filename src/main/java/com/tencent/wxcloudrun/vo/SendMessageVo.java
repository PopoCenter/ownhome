package com.tencent.wxcloudrun.vo;


/**
 * 引擎读取
 *
 * @author dongdongxie
 * @date 2023/07/12
 */
public class SendMessageVo {

    private static final long serialVersionUID = 382951780872523489L;

    /**
     * 引擎名称
     */
    private String name;

    /**
     * 引擎版本
     */
    private String version;

    /**
     * 厂商名称
     */
    private String provider;

    /**
     * cpu数量
     */
    private String cpu;

    /**
     * gpu数量
     */
    private String gpu;

    /**
     * memory数量
     */
    private String memory;

    /**
     * disk数量
     */
    private String disk;

    /**
     * 描述
     */
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}