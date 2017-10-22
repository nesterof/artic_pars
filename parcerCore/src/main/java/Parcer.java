import model.Article;

import java.io.IOException;
import java.util.List;

public interface Parcer {
    public Article parceMetadate();

    public List<Article> parce();

    public List<Article> parceTableContent() throws IOException;
}
