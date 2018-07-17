package com.otitan.sclyyq.entity;

/**
 * 附件实体类
 */
public class Attachment {
    //服务器端id
    private int Id = 0;
    //文件名
    private String FileName = "";
    // 1图片 2视频 3音频
    private int type = 1;
    //文件路径
    private String FileUrl = "";
    //文件json
    private String IoFile = "";
    //备注
    private String remark = "";

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public String getIoFile() {
        return IoFile;
    }

    public void setIoFile(String ioFile) {
        IoFile = ioFile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
