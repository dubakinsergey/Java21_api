package data;

import org.testng.annotations.DataProvider;

public class GetDataProvider {

    @DataProvider(name = "validPostIds")
    public static Object[][] validPostIds() {
        return new Object[][]{
                // postId, description
                {1, "пост с id=1"},
                {2, "пост с id=2"},
                {3, "пост с id=3"},
                {5, "пост с id=5"},
                {10, "пост с id=10"}
        };
    }

    @DataProvider(name = "invalidPostIds")
    public static Object[][] invalidPostIds() {
        return new Object[][]{
                // postId, description, expectedStatus
                {99999, "несуществующий id", 404},
                {-1, "отрицательный id", 404},
                {0, "id = 0", 404},
                {100000, "очень большой id", 404}
        };
    }

    @DataProvider(name = "postIdsInList")
    public static Object[][] postIdsInList() {
        return new Object[][]{
                {1, "пост с id=1 существует"},
                {2, "пост с id=2 существует"},
                {3, "пост с id=3 существует"},
                {5, "пост с id=5 существует"},
                {10, "пост с id=10 существует"}
        };
    }
}