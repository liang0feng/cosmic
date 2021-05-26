package kded.demo.fromplugin.dycreateentry;

import kd.bos.bill.MobileBillShowParameter;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.GetEntityTypeEventArgs;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDException;
import kd.bos.form.ClientProperties;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.container.Container;
import kd.bos.form.control.Control;
import kd.bos.form.events.LoadCustomControlMetasArgs;
import kd.bos.form.events.OnGetControlArgs;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.MobileListShowParameter;
import kd.bos.metadata.entity.commonfield.ComboItem;
import kd.bos.metadata.form.container.FlexPanelAp;
import kd.bos.metadata.form.control.EntryAp;

import java.util.*;

public class TestMobilPlugin extends AbstractFormPlugin {
    private String[] buttons = new String[]{"computersq","emailsq"};
    private final static String KEY_ENTRYENTITY = "entryentity";
    private final static String KEY_MYFIELDCONTAINER = "myfieldcontainer";
    private final static String KEY_AUTOTEXT1 = "autotext1";
    private final static String KEY_AUTOBIGTEXT1 = "autobigtext1";
    private final static String KEY_AUTOTEXT2 = "autotext2";
    private final static String KEY_AUTOBUTTON1 = "autobutton1";

/**
     * 界面显示前，触发此事件：向前端界面输出动态添加的字段、控件
     * @remark
     * 这个事件只能向前端界面添加字段、控件；后台的视图模型、数据模型，均没有改变
     */

    @Override
    public void loadCustomControlMetas(LoadCustomControlMetasArgs e) {
        super.loadCustomControlMetas(e);
        //文本
        DataElement data1 = new DataElement();
        data1.setTitleNum("text");
        data1.setTitleName("文本:");
        data1.setId("1");
        data1.setKey("textKey");//标识
        //大文本
        DataElement data2 = new DataElement();
        data2.setTitleNum("areaText");
        data2.setTitleName("多行文本:");
        data2.setId("2");
        data2.setKey("areaTextKey");//标识
        //日期
        DataElement data3 = new DataElement();
        data3.setTitleNum("date");
        data3.setTitleName("日期:");
        data3.setId("3");
        data3.setKey("dateKey");
        //长日期
        DataElement data4 = new DataElement();
        data4.setTitleNum("dateTime");
        data4.setTitleName("长日期:");
        data4.setId("4");
        data4.setKey("dateTimeKey");
        //日期范围
        DataElement data5 = new DataElement();
        data5.setTitleNum("dateRange");
        data5.setTitleName("日期范围:");
        data5.setId("5");
        data5.setKey("dateRangeKey");
        //下拉列表
        DataElement data6 = new DataElement();
        data6.setTitleNum("combo");
        data6.setTitleName("下拉列表:");
        data6.setId("6");
        data6.setKey("comboKey");
        List<ComboItem> comboItems = new ArrayList<>();
        comboItems.add(new ComboItem(1,new LocaleString("第一项"), "a"));
        comboItems.add(new ComboItem(2,new LocaleString("第二项"), "b"));
        data6.setComboItems(comboItems);
        //多选下拉列表
        DataElement data7 = new DataElement();
        data7.setTitleNum("mulCombo");
        data7.setTitleName("多选下拉列表:");
        data7.setId("7");
        data7.setKey("mulComboKey");
        List<ComboItem> mulcomboItems = new ArrayList<>();
        mulcomboItems.add(new ComboItem(1,new LocaleString("第一项"), "a"));
        mulcomboItems.add(new ComboItem(2,new LocaleString("第二项"), "b"));
        data7.setComboItems(mulcomboItems);
        //按钮
        DataElement data8 = new DataElement();
        data8.setTitleNum("button");
        data8.setTitleName("按钮:");
        data8.setId("8");
        data8.setKey("buttonKey");

        List<DataElement> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        dataList.add(data4);
        //dataList.add(data5);
        dataList.add(data6);
        dataList.add(data7);
        dataList.add(data8);

        DynamicPanel.createJSONObject(KEY_MYFIELDCONTAINER,dataList);//构建json
        Map<String, Object> mapHead = new HashMap<>();
        FlexPanelAp headPanelAp = DynamicPanel.createDynamicPanel();//创建需要动态生成的控件
        mapHead.put(ClientProperties.Id, KEY_MYFIELDCONTAINER);
        mapHead.put(ClientProperties.Items, headPanelAp.createControl().get(ClientProperties.Items));
        e.getItems().add(mapHead);
        // 动态添加单据体字段
//        EntryAp entryAp = this.createDynamicEntryAp();
//        Map<String, Object> mapEntry = new HashMap<>();
//        mapEntry.put(ClientProperties.Id, KEY_ENTRYENTITY);
//        mapEntry.put(ClientProperties.Columns, entryAp.createControl().get(ClientProperties.Columns));
//        e.getItems().add(mapEntry);
    }

/**
     * 此事件在系统要用到表单主实体模型时触发
     * @param e
     * @remark
     * 插件修改原始主实体，注册自定义属性，返回新的主实体给系统
     */

    @Override
    public void getEntityType(GetEntityTypeEventArgs e) {
        MainEntityType oldMainType = e.getOriginalEntityType();
        MainEntityType newMainType = null;
        try{
            newMainType = (MainEntityType)oldMainType.clone();
        }
        catch (CloneNotSupportedException exp){
            throw new KDException(exp, new ErrorCode("LoadCustomControlMetasSample", exp.getMessage()));
        }
        DynamicPanel.registerProp(newMainType);

//        // 向单据体动态注册一个新的文本属性
//        EntryType entryType = (EntryType)newMainType.getAllEntities().get(KEY_ENTRYENTITY);
//        TextProp textProp2 = new TextProp();
//        textProp2.setName(KEY_AUTOTEXT2);	// 标识
//        textProp2.setDisplayName(new LocaleString("自动文本2"));	// 标题
//        textProp2.setDbIgnore(true);	// 此字段不需到物理表格取数
//        textProp2.setAlias("");			// 物理字段名
//        // 把新字段，注册到单据体
//        entryType.registerSimpleProperty(textProp2);
//
        e.setNewEntityType(newMainType);
    }

/**
     * 此事件在表单创建界面数据包时触发
     * @remark
     * 由插件自行创建界面数据包，包含自定义字段
     *//*

    @Override
    public void createNewData(BizDataEventArgs e) {
        DynamicObject dataEntity = new DynamicObject(this.getModel().getDataEntityType());
        dataEntity.set("textKey", "动态文本字段的值");
        dataEntity.set("areaTextKey",
                "动态多行文本字段的值111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                        "1111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                        "111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                        "111111111111111111111111111111111111111111111111111111111111111111");
        dataEntity.set("dateKey", new Date());
        dataEntity.set("dateTimeKey", new Date());
        //dataEntity.set("dateRangeKey", new Date());
        dataEntity.set("comboKey", "a");
        dataEntity.set("mulComboKey", "a,b");

        // 需要通过如下方式，获取日期范围字段，开始、结束属性对象的标识
//        DateRangeEdit headFieldEdit = this.getView().getControl("dateRangeKey");
//        String key_headdatestart = headFieldEdit.getStartDateFieldKey();
//        String key_headdateend = headFieldEdit.getEndDateFieldKey();
//        // 获取单据头日期范围，开始时间、结束时间
//        Date headdatestart = (Date) this.getModel().getValue(key_headdatestart);
//        Date headdateend = (Date) this.getModel().getValue(key_headdateend);
//        // 赋值
//        this.getModel().setValue(key_headdatestart, headdatestart);
//        this.getModel().setValue(key_headdateend, headdateend);

        //单据体
//        DynamicObjectCollection rows = dataEntity.getDynamicObjectCollection(KEY_ENTRYENTITY);
//        DynamicObject newRow = new DynamicObject(rows.getDynamicObjectType());
//        newRow.set(KEY_AUTOTEXT2, "动态文本字段2的值");
//        rows.add(newRow);

        e.setDataEntity(dataEntity);
    }

/**
     * 此事件在把数据绑定到界面之前触发：
     * 系统会调用FormDataBinder对象，把字段值输出给前端字段编辑控件；
     * @param e
     * @remark
     * 动态添加的字段，在FormDataBinder中并没有记录，因此，默认不会绑定动态添加的字段值；
     * 必须在此事件，向FormDataBinder中注册动态添加的字段
     */

    @Override
    public void beforeBindData(EventObject e) {
        // 单据头添加的字段、控件，注入到容器面板的控件编程模型中
        FlexPanelAp dynamicPanel = DynamicPanel.createDynamicPanel();
        Container myFldPanel = this.getView().getControl(KEY_MYFIELDCONTAINER);
        myFldPanel.getItems().addAll(dynamicPanel.buildRuntimeControl().getItems());
        this.getView().createControlIndex(myFldPanel.getItems());

//        // 单据体添加的字段，注入到单据体表格的控件编程模型中
//        EntryAp dynamicEntryAp = this.createDynamicEntryAp();
//        EntryGrid entryGrid = this.getView().getControl(KEY_ENTRYENTITY);
//        List<Control> fieldEdits = dynamicEntryAp.buildRuntimeControl().getItems();
//        for(Control fieldEdit : fieldEdits){
//            fieldEdit.setView(this.getView());
//            entryGrid.getItems().add(fieldEdit);
//        }
    }

    @Override
    public void onGetControl(OnGetControlArgs e) {
//        super.onGetControl(e);
//        if (StringUtils.equals(KEY_AUTOTEXT1, e.getKey())){
//            TextEdit textEdit = new TextEdit();
//            textEdit.setKey(KEY_AUTOTEXT1);
//            textEdit.setView(this.getView());
//            e.setControl(textEdit);
//        }
    }

/**
     * 创建一个单据体表格，并向其中动态添加字段
     */

    private EntryAp createDynamicEntryAp(){
        EntryAp entryAp = new EntryAp();
//        entryAp.setKey("entryap");
//        // 动态添加一个文本字段
//        EntryFieldAp fieldAp = new EntryFieldAp();
//        fieldAp.setId(KEY_AUTOTEXT2);
//        fieldAp.setKey(KEY_AUTOTEXT2);
//        fieldAp.setName(new LocaleString("自动文本2"));
//        fieldAp.setFireUpdEvt(true);	// 即时触发值更新事件
//        TextField field = new TextField();
//        field.setId(KEY_AUTOTEXT2);
//        field.setKey(KEY_AUTOTEXT2);
//        fieldAp.setField(field);
//        entryAp.getItems().add(fieldAp);
        return entryAp;
    }

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addClickListeners(buttons);
    }

    @Override
    public void click(EventObject evt) {
        super.click(evt);
        Control control = (Control) evt.getSource();
        String key = control.getKey();
        MobileListShowParameter listShowParameter = new MobileListShowParameter();
        MobileBillShowParameter formShowParameter = new MobileBillShowParameter();
        FormShowParameter showParameter = new FormShowParameter();
        if("computersq".equals(key)){
            listShowParameter.setBillFormId("pk_cw_adminapply");
            listShowParameter.getOpenStyle().setShowType(ShowType.Floating);
            this.getView().showForm(listShowParameter);
//            formShowParameter.setFormId("pk_cw_computerreq");
//            formShowParameter.getOpenStyle().setShowType(ShowType.Floating);
//            this.getView().showForm(formShowParameter);
        }else if("emailsq".equals(key)){
            listShowParameter.setBillFormId("pk_cw_emailapply");
            listShowParameter.getOpenStyle().setShowType(ShowType.Floating);
            this.getView().showForm(listShowParameter);
        }
    }
}
