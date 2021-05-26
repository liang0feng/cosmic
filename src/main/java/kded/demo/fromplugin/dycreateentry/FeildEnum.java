package kded.demo.fromplugin.dycreateentry;

/**
 * @author rd_feng_liang
 * @date 2021/3/5
 */
public enum FeildEnum {
    /** 文本 */
    Text("text"),
    /** 多行文本 */
    AreaText("areaText"),
    /** 日期 */
    Date("date"),
    /** 长日期 */
    DateTime("dateTime"),
    /** 日期范围 */
    DateRange("dateRange"),
    /** 下拉列表 */
    Combo("combo"),
    /** 多选下拉列表 */
    MulCombo("mulCombo"),
    /** 按钮 */
    Button("button"),
    /** 未知 */
    Unknow("unKnow");

    private final String value;

    FeildEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

