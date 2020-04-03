package cn.csg.ucs.bi.business.entity.helper;

import cn.csg.core.common.annotation.OrderConfig;
import cn.csg.core.common.annotation.SetAlias;
import cn.csg.ucs.bi.business.entity.B_SELF_SAVE_BASE;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电网自身节电量节电力基础信息数据实体
 */
@Data
@Table(name = "B_SELF_SAVE_BASE")
@SetAlias("b_self_save_base")
@OrderConfig(FIELDS = "COMPANY ASC,PROJECT_CATEGORY, OPERATE_DATE")
public class SelfSaveBase extends B_SELF_SAVE_BASE {

    // 项目ID，必填
    @Id
    @Column(name = "PROJECT_ID")
    private String projectId;

    @Transient
    private String tableName;

    // 子信息集合
    private List<HashMap<String, Object>> subInfos;

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }

    public List<HashMap<String, Object>> getSubInfos() {
        return subInfos;
    }

    public void setSubInfos(List<HashMap<String, Object>> subInfos) {
        this.subInfos = subInfos;
    }
}
