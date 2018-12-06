package cn.mklaus.plugin;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import lombok.Data;
import lombok.ToString;

/**
 * @author klaus
 * @date 2018/12/6 9:47 AM
 */
@Data
@ToString
public class Column {

    private PsiField psiField;

    private PsiType psiType;

    private String type;

    private String name;

    private String capitalName;

    public Column(PsiField psiField) {
        this.psiField = psiField;
        this.name = psiField.getName();
        this.capitalName = this.name.substring(0,1).toUpperCase() + this.name.substring(1);
        this.psiType = psiField.getType();
        this.type = psiField.getType().getPresentableText();
    }
}
