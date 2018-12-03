package cn.mklaus.plugin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author klaus
 * @date 2018/11/29 2:56 PM
 */
@ToString
@Data
@NoArgsConstructor
public class Template {

    public enum PATH {
        /**
         * 当前项目路径，主包路径，resources路径
         */
        PROJECT_BASE, PACKAGE_BASE, RESOURCES_BASE;

        public static List<String> listName() {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < values().length; i++) {
                list.add(values()[i].name());
            }
            return list;
        }
    }

    public Template(String name) {
        this(name, "");
    }

    public Template(String name, String content) {
        this(name, content, "");
    }

    public Template(String name, String content, String prefixDir) {
        this.name = name;
        this.content = content;
        this.relativePath = PATH.PACKAGE_BASE;
        this.prefixDir = prefixDir;
    }

    private String name;

    private String content;

    private PATH relativePath;

    private String prefixDir;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Template)) {
            return false;
        }
        Template template = (Template) o;
        return Objects.equals(getName(), template.getName()) &&
                Objects.equals(getContent(), template.getContent()) &&
                getRelativePath() == template.getRelativePath() &&
                Objects.equals(getPrefixDir(), template.getPrefixDir());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContent(), getRelativePath(), getPrefixDir());
    }

    @Override
    public Template clone() {
        Template template = new Template();
        template.name = this.name;
        template.content = this.content;
        template.relativePath = this.relativePath;
        template.prefixDir = this.prefixDir;
        return template;
    }
}
