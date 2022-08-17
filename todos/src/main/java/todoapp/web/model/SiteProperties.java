package todoapp.web.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 사이트(site) 정보 모델
 *
 * @author springrunner.kr@gmail.com
 */
@ConfigurationProperties(prefix = "site") 
public class SiteProperties {

    private String author = "unknown";
    private String description = "TodoApp templates for Server-side";

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SiteProperties{");
        sb.append("author='").append(author).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
