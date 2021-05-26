package kded.demo.fromplugin.dycreateentry;


import kd.bos.metadata.entity.commonfield.ComboItem;

import java.util.List;

/**
 * @author rd_feng_liang
 * @date 2021/3/5
 */
public class DataElement {
    private String titleNum;// 控件种类
    private String titleName;// 控件名称
    private String id;// 控件id
    private String key;// 控件key
    private List<ComboItem> comboItems;//下拉值

    public DataElement(String titleNum, String titleName, String id, String key, List<ComboItem> comboItems) {
        this.titleNum = titleNum;
        this.titleName = titleName;
        this.id = id;
        this.key = key;
        this.comboItems = comboItems;
    }

    public DataElement() {

    }

    public String getTitleNum() {
        return titleNum;
    }

    public void setTitleNum(String titleNum) {
        this.titleNum = titleNum;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<ComboItem> getComboItems() {
        return comboItems;
    }

    public void setComboItems(List<ComboItem> comboItems) {
        this.comboItems = comboItems;
    }

}
