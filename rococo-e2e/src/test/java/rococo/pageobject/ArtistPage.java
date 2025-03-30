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

public class ArtistPage extends BasePage<ArtistPage>{

    private final ElementsCollection listArtis = $$("main[id='page-content'] li a span");
    private final SelenideElement addArtistButton = $("div.justify-between button.variant-filled-primary");
    private final   SearchBlock searchBlock = new SearchBlock();


    @Step("Открываем карточку художника")
    public ArtistCardPage openArtistCard(String nameArtist){
        listArtis.find(text(nameArtist)).click();
        return new ArtistCardPage();
    }

    @Step("Открываем карточку на редактирование для создания нового художника")
    public EditArtistPage openAddArtistCard(){
        addArtistButton.click();
        return new EditArtistPage();
    }

    @Step("Проверяем отображение кнопки добавления художника, если состояния авторизации пользователя = {needVisibleButton}")
    public ArtistPage checkVisibleAddArtistButton(boolean needVisibleButton){
        Assertions.assertEquals(
                needVisibleButton,
                addArtistButton.isDisplayed()
        );
        return this;
    }

    @Step("Ищем художника по имени")
    public ArtistPage searchArtistByName(String nameArtist){
        searchBlock.search(nameArtist);
        listArtis.find(text(nameArtist)).should(visible);
        return this;
    }
}
