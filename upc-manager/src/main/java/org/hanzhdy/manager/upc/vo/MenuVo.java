package org.hanzhdy.manager.upc.vo;

/**
 * @description 菜单VO
 * @author H.CAAHN
 * @createtime 2016年6月29日 下午8:25:45
 */
public class MenuVo {
    /** ID号 */
    private Long   id;
    
    /** 菜单编码 */
    private String code;
    
    /** 菜单名称 */
    private String name;
    
    /** 菜单类型 */
    private String type;
    
    /** 地址类型 */
    private String urltype;
    
    /** 菜单地址 */
    private String url;
    
    /** 菜单状态 */
    private String status;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUrltype() {
        return urltype;
    }
    
    public void setUrltype(String urltype) {
        this.urltype = urltype;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
