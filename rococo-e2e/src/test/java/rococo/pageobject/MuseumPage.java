package rococo.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import rococo.pageobject.component.Header;
import rococo.pageobject.component.MessageAlert;
import rococo.pageobject.component.SearchBlock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MuseumPage extends BasePage<MuseumPage> {

    private final ElementsCollection listMuseums = $$("main[id='page-content'] li a div");
    private final SelenideElement addMuseumButton = $("main[id='page-content'] div button.variant-filled-primary");

    private final SearchBlock searchBlock = new SearchBlock();
    private final MessageAlert messageAlert = new MessageAlert();

    @Step("Открываем карточку музея")
    public MuseumCardPage openMuseumCard(String namePainting) {
        listMuseums.find(text(namePainting)).click();
        return new MuseumCardPage();
    }

    @Step("Открываем форму для добавления музея")
    public EditMuseumPage openCardToAddMuseum() {
        addMuseumButton.click();
        return new EditMuseumPage();
    }

    @Step("Проверяем отображение кнопки добавления музея, если состояния авторизации пользователя = {needVisibleButton}")
    public MuseumPage checkVisibleAddMuseumButton(boolean needVisibleButton) {
        Assertions.assertEquals(
                needVisibleButton,
                addMuseumButton.isDisplayed()
        );
        return this;
    }

    @Step("Ищем музей по названию")
    public MuseumPage searchMuseumByName(String nameMuseum) {
        searchBlock.search(nameMuseum);
        listMuseums.find(text(nameMuseum)).should(visible);
        return this;
    }

    @Step("Закрываем сообщение")
    public MuseumPage closeMessageAlert() {
        messageAlert.closeMessage();
        return this;
    }
}
