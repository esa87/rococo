package rococo.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import rococo.pageobject.component.SearchBlock;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaintingPage extends BasePage<PaintingPage> {

    private final ElementsCollection listPaintings = $$("main[id='page-content'] li a div");
    private final SelenideElement addPaintingButton = $("main[id='page-content'] button.variant-filled-primary");

    private final SearchBlock searchBlock = new SearchBlock();

    @Step("Открываем карточку картины")
    public PaintingCardPage openPaintingCard(String namePainting) {
        listPaintings.find(text(namePainting)).click();
        return new PaintingCardPage();
    }

    @Step("Добавляем картину")
    public EditPaintingPage addPaintingCard() {
        addPaintingButton.click();
        return new EditPaintingPage();
    }

    @Step("Проверяем отображение кнопки добавления картины, если состояния авторизации пользователя = {needVisibleButton}")
    public PaintingPage checkVisibleAddPaintingButton(boolean needVisibleButton) {
        Assertions.assertEquals(
                needVisibleButton,
                addPaintingButton.isDisplayed()
        );
        return this;
    }


    @Step("Ищем картину по названию")
    public PaintingPage searchPaintingByTitle(String titlePainting){
        searchBlock.search(titlePainting);
        listPaintings.find(text(titlePainting)).should(visible);
        return this;
    }





}
