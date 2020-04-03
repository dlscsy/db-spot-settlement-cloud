package cn.csg.ucs.bi.common.structure;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    // 显示值
    private String label;

    // 实际值
    private String value;

    // 子节点
    private List<TreeNode> childrenNodes = new ArrayList<TreeNode>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<TreeNode> getChildrenNodes() {
        return childrenNodes;
    }

    public void setChildrenNodes(List<TreeNode> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }
}
