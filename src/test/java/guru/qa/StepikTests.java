package guru.qa;

import com.codeborne.selenide.collections.ContainExactTextsCaseSensitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class StepikTests {

    @BeforeEach
    void setUp() {

        open("https://stepik.org/catalog");
    }

    static Stream<Arguments> stepikCatalogTest() {

        return Stream.of(
                Arguments.of("English", "Editors' choice", List.of("Ace Your Next Coding Interview by Learning Algorithms", "LaunchCode's Discovery",
                        "Data Structures", "Bioinformatics Algorithms: An Active Learning Approach", "Linear Algebra: Problems and Methods", "Atomic Kotlin")),
                Arguments.of("Русский", "Веб-разработка", List.of("Профессия веб верстальщик. Web разработка и создание сайтов 2022", "Веб дизайн в Figma. Анимации и Главные правила \"Юзабилити\".",
                        "Теория цифрового дизайна. Веб дизайн, фотография, композиция!", "Верстка и веб-разработка сайтов. Продвинутый уровень Web Develop", "Верстка и веб разработка сайтов 2022 - с нуля! Web development",
                        "WEB программирование на ASP.NET Core. ВСЕ САМ"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Проверка наличия карточек в блоке {1} на странице https://stepik.org/catalog с локалью {0}")
    @Tag("CRITICAL")
    void stepikCatalogTest(String locale, String expectedButton, List<String> cards) {

        $(".st-button_style_none").click();
        $$(".drop-down-content li button").find(text(locale)).click();
        $(".catalog-block-full-course-lists__tablist li button").shouldHave(text(expectedButton));
        $$(".course-card__title").shouldHave(new ContainExactTextsCaseSensitive(cards));
    }


    @CsvSource({
            "Kotlin, Android профессиональный уровень (Kotlin)",
            "SQL, SQL для всех"
    })
    @ParameterizedTest(name = "Проверка наличия курса {1} в каталоге сайта https://stepik.org/catalog по запросу {0}")
    @Tags({@Tag("BLOCKER"), @Tag("FEATURE")})
    void stepikSearchCourses(String keyword, String course) {

        $(".search-form__input ").setValue(keyword).pressEnter();
        $(".catalog-block__content").shouldHave(text(course));
    }
}
