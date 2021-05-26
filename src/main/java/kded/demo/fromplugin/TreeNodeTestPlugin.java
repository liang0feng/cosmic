package kded.demo.fromplugin;

import kd.bos.entity.tree.TreeNode;
import kd.bos.form.AbstractFormView;
import kd.bos.form.control.TreeView;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.QueryServiceHelper;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rd_feng_liang
 * @date 2020/11/16
 */
public class TreeNodeTestPlugin extends AbstractFormPlugin {

    @Override
    public void afterCreateNewData(EventObject e) {
        TreeView tv1 = this.getView().getControl("kded_treeviewap");
        this.constructorData(tv1);
        Map<String, Object> params = new HashMap<>();
        params.put("fs", "48px");
        this.getView().updateControlMetadata("kded_treeviewap", params);
    }

    /**
     * 构造数据
     */
    private void constructorData(TreeView tv1) {
        final String rootId = "0"; // 根节点id
        TreeNode rootNode = new TreeNode(null, rootId, "根节点", true);
        rootNode.setIsOpened(true); // 设置默认展开
        for (int i = 0; i < 2; i++) {
            // 构造一级子节点
            String p1 = rootId + "-" + i;
            TreeNode tn1 = new TreeNode(rootId, p1, "分组1", true);
            tn1.setIsOpened(true);
            tn1.setColor("red"); // 设置节点文字颜色
            for (int j = 0; j < 2; j++) {
                // 构造二级子节点
                String p2 = p1 + "-" + j;
                TreeNode tn2 = new TreeNode(p1, p2, p2, false);
                tn1.addChild(tn2);
            }
            rootNode.addChild(tn1);
        }
        tv1.addNode(rootNode);
    }

}
