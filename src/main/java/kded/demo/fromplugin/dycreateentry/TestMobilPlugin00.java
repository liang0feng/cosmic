package kded.demo.fromplugin.dycreateentry;

import com.alibaba.fastjson.JSONObject;
import kd.bos.bill.MobileBillShowParameter;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.entity.datamodel.events.GetEntityTypeEventArgs;
import kd.bos.entity.property.TextProp;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDException;
import kd.bos.form.ClientProperties;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.container.Container;
import kd.bos.form.control.Control;
import kd.bos.form.events.LoadCustomControlMetasArgs;
import kd.bos.form.events.OnGetControlArgs;
import kd.bos.form.field.TextEdit;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.MobileListShowParameter;
import kd.bos.metadata.entity.commonfield.*;
import kd.bos.metadata.form.container.FlexPanelAp;
import kd.bos.metadata.form.control.ButtonAp;
import kd.bos.metadata.form.control.EntryAp;
import kd.bos.metadata.form.control.EntryFieldAp;
import kd.bos.metadata.form.control.FieldAp;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class TestMobilPlugin00 extends AbstractFormPlugin {
    private String[] buttons = new String[]{"computersq","emailsq"};

    private final static String KEY_ENTRYENTITY = "entryentity";
    private final static String KEY_MYFIELDCONTAINER = "pk_testydbd";
    private final static String KEY_AUTOTEXT1 = "autotext1";
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
//        String containerKey = "pk_testydbd";
//        //文本
//        DataElement data1 = new DataElement();
//        data1.setTitleNum("text");
//        data1.setTitleName("自动文本:");
//        data1.setId("1");
//        data1.setKey("1");
//        //大文本
//        DataElement data2 = new DataElement();
//        data2.setTitleNum("bigText");
//        data2.setTitleName("自动大文本:");
//        data2.setId("2");
//        data2.setKey("2");
//        //日期
//        DataElement data3 = new DataElement();
//        data3.setTitleNum("date");
//        data3.setTitleName("自动日期:");
//        data3.setId("3");
//        data3.setKey("3");
//        List<DataElement> dataList = new ArrayList<>();
//        dataList.add(data1);
//        //dataList.add(data2);
//        //dataList.add(data3);
//        JSONObject jsonObject = DynamicPanel.createJSONObject(containerKey,dataList);
//        System.out.println("json:" + jsonObject.toString());
//        Map<String, Object> mapHead = DynamicPanel.createDynamicPanel(jsonObject);
//        e.getItems().add(mapHead);

        // 动态添加单据头字段、按钮
        FlexPanelAp headAp = this.createDynamicPanel();
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put(ClientProperties.Id, KEY_MYFIELDCONTAINER);
        mapHead.put(ClientProperties.Items, headAp.createControl().get(ClientProperties.Items));
        e.getItems().add(mapHead);
//
//        // 动态添加单据体字段
//        EntryAp entryAp = this.createDynamicEntryAp();
//        Map<String, Object> mapEntry = new HashMap<>();
//        mapEntry.put(ClientProperties.Id, KEY_ENTRYENTITY);
//        mapEntry.put(ClientProperties.Columns, entryAp.createControl().get(ClientProperties.Columns));
//        e.getItems().add(mapEntry);
    }

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
        // 向单据头动态注册一个新的文本属性
        // 把新字段，注册到单据头
        TextProp textProp1 = new TextProp();
        textProp1.setName(KEY_AUTOTEXT1);	// 标识
        textProp1.setDisplayName(new LocaleString("自动文本1"));	// 标题
        textProp1.setDbIgnore(true);	// 此字段不需到物理表格取数
        textProp1.setAlias("");			// 物理字段名
        newMainType.registerSimpleProperty(textProp1);
        e.setNewEntityType(newMainType);
    }

    @Override
    public void afterCreateNewData(EventObject e) {
        DynamicObject dataEntity = new DynamicObject(this.getModel().getDataEntityType());
        dataEntity.set(KEY_AUTOTEXT1, "动态文本字段1的值");
    }

    @Override
    public void onGetControl(OnGetControlArgs e) {
        super.onGetControl(e);
        if (StringUtils.equals(KEY_AUTOTEXT1, e.getKey())){
            TextEdit textEdit = new TextEdit();
            textEdit.setKey(KEY_AUTOTEXT1);
            textEdit.setView(this.getView());
            e.setControl(textEdit);
        }
    }

    @Override
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        // 单据头添加的字段、控件，注入到容器面板的控件编程模型中
        FlexPanelAp dynamicPanel = this.createDynamicPanel();
        Container myFldPanel = this.getView().getControl("kded_flexpanelap");
        myFldPanel.getItems().addAll(dynamicPanel.buildRuntimeControl().getItems());
        this.getView().createControlIndex(myFldPanel.getItems());
    }

    /**
     * 创建一个面板，并向其中动态添加字段、控件
     */
    private FlexPanelAp createDynamicPanel(){
        FlexPanelAp headPanelAp = new FlexPanelAp();
        headPanelAp.setKey("headAp");
        // 动态添加一个文本字段
        FieldAp fieldAp = new FieldAp();
        fieldAp.setId(KEY_AUTOTEXT1);
        fieldAp.setKey(KEY_AUTOTEXT1);
        fieldAp.setName(new LocaleString("自动文本:"));
        fieldAp.setBackColor("#FFFFFF");
        fieldAp.setFireUpdEvt(true);	// 即时触发值更新事件
        TextField field = new TextField();
        field.setId(KEY_AUTOTEXT1);
        field.setKey(KEY_AUTOTEXT1);
        fieldAp.setField(field);
        headPanelAp.getItems().add(fieldAp);
        // 动态添加一个多行文本
//        FieldAp fieldAp2 = new FieldAp();
//        fieldAp2.setId("autotext3");
//        fieldAp2.setKey("autotext3");
//        fieldAp2.setName(new LocaleString("自动多行文本:"));
////        fieldAp2.setBackColor("#FFFFFF");
////        fieldAp2.setFireUpdEvt(true);	// 即时触发值更新事件
//        TextAreaField textAreaField = new TextAreaField();
//        textAreaField.setId("autotext3");
//        textAreaField.setKey("autotext3");
//        fieldAp2.setField(textAreaField);
//        headPanelAp.getItems().add(fieldAp2);
//        // 动态添加一个大文本
//        FieldAp fieldAp3 = new FieldAp();
//        fieldAp3.setId("autotext4");
//        fieldAp3.setKey("autotext4");
//        fieldAp3.setName(new LocaleString("自动大文本:"));
////        fieldAp3.setBackColor("#FFFFFF");
////        fieldAp3.setFireUpdEvt(true);	// 即时触发值更新事件
//        LargeTextField largeTextField = new LargeTextField();
//        largeTextField.setId("autotext4");
//        largeTextField.setKey("autotext4");
//        fieldAp3.setField(largeTextField);
//        headPanelAp.getItems().add(fieldAp3);
//        // 动态添加一个下拉框
//        FieldAp fieldAp4 = new FieldAp();
//        fieldAp4.setId("autotext5");
//        fieldAp4.setKey("autotext5");
//        fieldAp4.setName(new LocaleString("自动下拉框:"));
//        fieldAp4.setBackColor("#FFFFFF");
//        fieldAp4.setFireUpdEvt(true);	// 即时触发值更新事件
//        ComboField comboField = new ComboField();
//        comboField.setId("autotext5");
//        comboField.setKey("autotext5");
//        List<ComboItem> items = new ArrayList<>();
//        items.add(new ComboItem(1,new LocaleString("第一项"), "a"));
//        items.add(new ComboItem(2,new LocaleString("第二项"), "b"));
//        comboField.setItems(items);
//        fieldAp4.setField(comboField);
//        headPanelAp.getItems().add(fieldAp4);
//
//        // 动态添加一个按钮
//        ButtonAp buttonAp = new ButtonAp();
//        buttonAp.setId(KEY_AUTOBUTTON1);
//        buttonAp.setKey(KEY_AUTOBUTTON1);
//        buttonAp.setName(new LocaleString("自动添加的按钮"));
//        headPanelAp.getItems().add(buttonAp);
        return headPanelAp;
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
