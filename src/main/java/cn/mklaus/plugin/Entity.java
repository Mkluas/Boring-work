package cn.mklaus.plugin;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author klaus
 * @date 2018/12/6 9:45 AM
 */
@Data
@ToString
public class Entity {

    private PsiClass psiClass;

    private String qualifiedName;

    private String name;

    private String camelName;

    private List<Column> columns;


    public Entity(PsiClass psiClass) {
        this.psiClass = psiClass;
        this.qualifiedName = psiClass.getQualifiedName();
        this.name = psiClass.getName();
        this.camelName = name.substring(0,1).toLowerCase() + name.substring(1);

        columns = new ArrayList<>();
        for(PsiField field : psiClass.getFields()) {
            columns.add(new Column(field));
        }
    }

}
