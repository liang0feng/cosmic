package kded.demo.fromplugin.dycreateentry;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.EntryType;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.entity.datamodel.events.GetEntityTypeEventArgs;
import kd.bos.entity.property.TextProp;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDException;
import kd.bos.form.ClientProperties;
import kd.bos.form.container.Container;
import kd.bos.form.control.Button;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.LoadCustomControlMetasArgs;
import kd.bos.form.events.OnGetControlArgs;
import kd.bos.form.field.TextEdit;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.metadata.entity.commonfield.TextField;
import kd.bos.metadata.form.container.FlexPanelAp;
import kd.bos.metadata.form.control.ButtonAp;
import kd.bos.metadata.form.control.EntryAp;
import kd.bos.metadata.form.control.EntryFieldAp;
import kd.bos.metadata.form.control.FieldAp;

public class Demo extends AbstractFormPlugin implements ClickListener {

	private final static String KEY_ENTRYENTITY = "entryentity";
	private final static String KEY_MYFIELDCONTAINER = "myfieldcontainer";
	private final static String KEY_AUTOTEXT1 = "autotext1";
	private final static String KEY_AUTOTEXT2 = "autotext2";
	private final static String KEY_AUTOBUTTON1 = "autobutton1";

	/**
	 * 界面显示前，触发此事件：向前端界面输出动态添加的字段、控件
	 * 
	 * @remark 这个事件只能向前端界面添加字段、控件；后台的视图模型、数据模型，均没有改变
	 */
	@Override
	public void loadCustomControlMetas(LoadCustomControlMetasArgs e) {
		super.loadCustomControlMetas(e);

		// 动态添加单据头字段、按钮
		FlexPanelAp headAp = this.createDynamicPanel();
		Map<String, Object> mapHead = new HashMap<>();
		mapHead.put(ClientProperties.Id, KEY_MYFIELDCONTAINER);
		mapHead.put(ClientProperties.Items, headAp.createControl().get(ClientProperties.Items));
		e.getItems().add(mapHead);

		// 动态添加单据体字段
		EntryAp entryAp = this.createDynamicEntryAp();
		Map<String, Object> mapEntry = new HashMap<>();
		mapEntry.put(ClientProperties.Id, KEY_ENTRYENTITY);
		mapEntry.put(ClientProperties.Columns, entryAp.createControl().get(ClientProperties.Columns));
		e.getItems().add(mapEntry);
	}

	/**
	 * 此事件在系统要用到表单主实体模型时触发
	 * 
	 * @param e
	 * @remark 插件修改原始主实体，注册自定义属性，返回新的主实体给系统
	 */
	@Override
	public void getEntityType(GetEntityTypeEventArgs e) {

		// 取原始的主实体
		MainEntityType oldMainType = e.getOriginalEntityType();
		// 复制主实体
		MainEntityType newMainType = null;
		try {
			newMainType = (MainEntityType) oldMainType.clone();
		} catch (CloneNotSupportedException exp) {
			throw new KDException(exp, new ErrorCode("LoadCustomControlMetasSample", exp.getMessage()));
		}

		// 为自定义的文本字段，向主实体注册文本属性
		this.registDynamicProps(newMainType);

		// 回传主实体给系统
		e.setNewEntityType(newMainType);
	}

	/**
	 * 此事件在表单创建界面数据包时触发
	 * 
	 * @remark 由插件自行创建界面数据包，包含自定义字段
	 */
	@Override
	public void createNewData(BizDataEventArgs e) {

		DynamicObject dataEntity = new DynamicObject(this.getModel().getDataEntityType());
		dataEntity.set(KEY_AUTOTEXT1, "动态文本字段1的值");

		DynamicObjectCollection rows = dataEntity.getDynamicObjectCollection(KEY_ENTRYENTITY);
		DynamicObject newRow = new DynamicObject(rows.getDynamicObjectType());
		newRow.set(KEY_AUTOTEXT2, "动态文本字段2的值");

		rows.add(newRow);

		e.setDataEntity(dataEntity);
	}

	/**
	 * 此事件在把数据绑定到界面之前触发： 系统会调用FormDataBinder对象，把字段值输出给前端字段编辑控件；
	 * 
	 * @param e
	 * @remark 动态添加的字段，在FormDataBinder中并没有记录，因此，默认不会绑定动态添加的字段值；
	 *         必须在此事件，向FormDataBinder中注册动态添加的字段
	 */
	@Override
	public void beforeBindData(EventObject e) {

		// 单据头添加的字段、控件，注入到容器面板的控件编程模型中
		FlexPanelAp dynamicPanel = this.createDynamicPanel();
		Container myFldPanel = this.getView().getControl(KEY_MYFIELDCONTAINER);
		myFldPanel.getItems().addAll(dynamicPanel.buildRuntimeControl().getItems());
		this.getView().createControlIndex(myFldPanel.getItems());

		// 单据体添加的字段，注入到单据体表格的控件编程模型中
		EntryAp dynamicEntryAp = this.createDynamicEntryAp();
		EntryGrid entryGrid = this.getView().getControl(KEY_ENTRYENTITY);
		List<Control> fieldEdits = dynamicEntryAp.buildRuntimeControl().getItems();
		for (Control fieldEdit : fieldEdits) {
			fieldEdit.setView(this.getView());
			entryGrid.getItems().add(fieldEdit);
		}
	}

	/**
	 * 用户与自定义的控件进行交互时，会触发此事件
	 * 
	 * @remark 插件在此事件中，创建自定义控件的编程模型，并侦听其事件
	 */
	@Override
	public void onGetControl(OnGetControlArgs e) {
		if (StringUtils.equals(KEY_AUTOBUTTON1, e.getKey())) {
			// 用户点击按钮时，会触发此事件：创建按钮的控件编程模型Button实例返回

			Button button = new Button();
			button.setKey(KEY_AUTOBUTTON1); // 必须
			button.setView(this.getView()); // 必须

			button.addClickListener(this);
			e.setControl(button);
		} else if (StringUtils.equals(KEY_AUTOTEXT1, e.getKey())) {
			// 用户修改了文本1字段值，前端上传字段值时，会先取字段的控件编程模型；
			// 如果没有本段代码，字段值上传失败，不会修改到数据模型中
			TextEdit textEdit = new TextEdit();
			textEdit.setKey(KEY_AUTOTEXT1);
			textEdit.setView(this.getView());
			e.setControl(textEdit);
		} else if (StringUtils.equals(KEY_AUTOTEXT2, e.getKey())) {
			TextEdit textEdit = new TextEdit();
			textEdit.setKey(KEY_AUTOTEXT2);
			textEdit.setEntryKey(KEY_ENTRYENTITY);
			textEdit.setView(this.getView());
			e.setControl(textEdit);
		}
	}

	@Override
	public void click(EventObject evt) {
		super.click(evt);
		Control source = (Control) evt.getSource();
		if (StringUtils.equals(source.getKey(), KEY_AUTOBUTTON1)) {
			String text1 = (String) this.getModel().getValue(KEY_AUTOTEXT1);
			this.getView().showMessage("您点击了动态添加的按钮1，此时动态文本字段1的值是：" + text1);
		}
	}

	/**
	 * 创建一个面板，并向其中动态添加字段、控件
	 */
	private FlexPanelAp createDynamicPanel() {

		FlexPanelAp headPanelAp = new FlexPanelAp();
		headPanelAp.setKey("headAp");

		// 动态添加一个文本字段
		FieldAp fieldAp = new FieldAp();
		fieldAp.setId(KEY_AUTOTEXT1);
		fieldAp.setKey(KEY_AUTOTEXT1);
		fieldAp.setName(new LocaleString("自动文本1"));
		fieldAp.setBackColor("#FFFFFF");
		fieldAp.setFireUpdEvt(true); // 即时触发值更新事件

		TextField field = new TextField();
		field.setId(KEY_AUTOTEXT1);
		field.setKey(KEY_AUTOTEXT1);
		fieldAp.setField(field);

		headPanelAp.getItems().add(fieldAp);

		// 动态添加一个按钮
		ButtonAp buttonAp = new ButtonAp();
		buttonAp.setId(KEY_AUTOBUTTON1);
		buttonAp.setKey(KEY_AUTOBUTTON1);
		buttonAp.setName(new LocaleString("自动添加的按钮"));

		headPanelAp.getItems().add(buttonAp);

		return headPanelAp;
	}

	/**
	 * 创建一个单据体表格，并向其中动态添加字段
	 */
	private EntryAp createDynamicEntryAp() {

		EntryAp entryAp = new EntryAp();
		entryAp.setKey("entryap");

		// 动态添加一个文本字段
		EntryFieldAp fieldAp = new EntryFieldAp();
		fieldAp.setId(KEY_AUTOTEXT2);
		fieldAp.setKey(KEY_AUTOTEXT2);
		fieldAp.setName(new LocaleString("自动文本2"));
		fieldAp.setFireUpdEvt(true); // 即时触发值更新事件

		TextField field = new TextField();
		field.setId(KEY_AUTOTEXT2);
		field.setKey(KEY_AUTOTEXT2);
		fieldAp.setField(field);

		entryAp.getItems().add(fieldAp);

		return entryAp;
	}

	/**
	 * 向主实体注册动态添加的属性
	 */
	private void registDynamicProps(MainEntityType newMainType) {
		// 向单据头动态注册一个新的文本属性
		TextProp textProp1 = new TextProp();

		textProp1.setName(KEY_AUTOTEXT1); // 标识
		textProp1.setDisplayName(new LocaleString("自动文本1")); // 标题

		textProp1.setDbIgnore(true); // 此字段不需到物理表格取数
		textProp1.setAlias(""); // 物理字段名

		// 把新字段，注册到单据头
		newMainType.registerSimpleProperty(textProp1);

		// 向单据体动态注册一个新的文本属性
		EntryType entryType = (EntryType) newMainType.getAllEntities().get(KEY_ENTRYENTITY);

		TextProp textProp2 = new TextProp();

		textProp2.setName(KEY_AUTOTEXT2); // 标识
		textProp2.setDisplayName(new LocaleString("自动文本2")); // 标题

		textProp2.setDbIgnore(true); // 此字段不需到物理表格取数
		textProp2.setAlias(""); // 物理字段名

		// 把新字段，注册到单据体
		entryType.registerSimpleProperty(textProp2);
	}
}