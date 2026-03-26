package data;

import org.testng.annotations.DataProvider;

public class PostDataProvider {

    @DataProvider(name = "happyPathPostData")
    public static Object[][] happyPathPostData() {
        return new Object[][]{
                {"Хасл учит POST", "Теперь я умею создавать данные", 1, "обычный пост"},
                {"Заголовок с пробелами", "Тело с пробелами", 2, "пробелы в данных"},
                {"Заголовок", "Тело", 100, "средний userId"},
                {"a".repeat(500), "b".repeat(500), 1, "длинные строки (500 символов)"}
        };
    }

    @DataProvider(name = "boundaryPostData")
    public static Object[][] boundaryPostData() {
        return new Object[][]{
                {"", "Тело поста", 1, "пустой заголовок", 201},
                {"Заголовок", null, 1, "пустое тело", 201},
                {"", null, 1, "пустой заголовок и тело", 201},
                {"a".repeat(2000), "Тело", 1, "очень длинный заголовок (2000)", 201},
                {"Заголовок", "b".repeat(2000), 1, "очень длинное тело (2000)", 201},
                {"Заголовок", "Тело", Integer.MAX_VALUE, "максимальный userId", 201},
                {"Заголовок", "Тело", 1, "контрольная точка", 201}
        };
    }

    @DataProvider(name = "negativePostData")
    public static Object[][] negativePostData() {
        return new Object[][]{
                {null, "Тело", 1, "title = null", 400},
                {"", "Тело", 1, "title = пустая строка", 400},
                {"Заголовок", null, 1, "body = null", 400},
                {"Заголовок", "Тело", -1, "userId отрицательный", 400},
                {"Заголовок", "Тело", 0, "userId = 0", 400},
                {"Заголовок", "Тело", -100, "userId сильно отрицательный", 400},
                {null, null, 1, "title и body = null", 400},
                {"", "", 1, "пустые title и body", 400}
        };
    }
}